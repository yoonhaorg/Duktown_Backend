package com.duktown.domain.comment.service;

import com.duktown.domain.comment.dto.CommentDto;
import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.comment.entity.CommentRepository;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.post.entity.PostRepository;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.like.entity.LikeRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final DeliveryRepository deliveryRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public void createComment(Long userId, CommentDto.CreateRequest request){
        // null이 아닌 필드가 여러 개일 때(대댓글 경우 제외) -> 잘못된 댓글 요청
        if (Stream.of(
                        request.getDeliveryId(),
                        request.getPostId())
                .filter(Objects::nonNull)
                .count() >= 2) {
            throw new CustomException(COMMENT_TARGET_ERROR);
        }

        // 사용자
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 부모 댓글
        Comment parentComment = null;
        if(request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new CustomException(PARENT_COMMENT_NOT_FOUND));

            if(parentComment.getParentComment() != null){
                throw new CustomException(COMMENT_DEPTH_ERROR);
            }
        }

        Delivery delivery = null;
        Post post = null;
        String userTitle;
        if(request.getDeliveryId() != null){
            delivery = deliveryRepository.findById(request.getDeliveryId())
                    .orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));

            // 글쓴이라면
            if (delivery.getUser().getId().equals(userId)) {
                userTitle = "글쓴이";
            }
            // 댓글 작성 유저가 해당 게시글에 댓글 단 내역이 없으면 현재 댓글 단 유저 수에 + 1한 값을 익명 숫자로 부여
            else if (!commentRepository.existsByUserIdAndDeliveryId(userId, delivery.getId())) {
                userTitle = "익명" + (commentRepository.countUniqueUsersByDeliveryIdExceptWriter(delivery.getId(), userId) + 1);
            }
            // 댓글 단 내역이 있다면 유저 아이디와 게시글 아이디로 댓글 한 개만 찾아 해당 댓글의 익명 숫자를 현재 댓글 익명 숫자로 부여
            else {
                userTitle = commentRepository.findFirstByUserIdAndDeliveryId(userId, delivery.getId()).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND))
                        .getUserTitle();
            }
        } else if (request.getPostId() != null) {
            post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
            // 글쓴이라면
            if (post.getUser().getId().equals(userId)) {
                userTitle = "글쓴이";
            } else if (!commentRepository.existsByUserIdAndPostId(userId, post.getId())) {
                userTitle = "익명" + (commentRepository.countUniqueUsersByPostIdExceptWriter(post.getId(), userId) + 1);
            } else {
                userTitle = commentRepository.findFirstByUserIdAndPostId(userId, post.getId()).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND))
                        .getUserTitle();
            }
        } else {
            throw new CustomException(COMMENT_TARGET_NOT_SELECTED);
        }

        Comment comment = request.toEntity(user, delivery, post, parentComment, userTitle);
        commentRepository.save(comment);
    }

    // TODO: queryDsl 도입해 대댓글 조회 기능 수정
    @Transactional(readOnly = true)
    public CommentDto.ListResponse getCommentList(Long userId, Long deliveryId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // null이 아닌 필드가 여러 개일 때(대댓글 경우 제외) -> 잘못된 댓글 요청
        if (Stream.of(
                        deliveryId,
                        postId)
                .filter(Objects::nonNull)
                .count() >= 2) {
            throw new CustomException(COMMENT_TARGET_ERROR);
        }

        List<Comment> comments;
        Long commentCount;
        List<Like> likes;

        if(deliveryId != null){
            // 배달팟 존재 확인
            deliveryRepository.findById(deliveryId).orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));
            comments = commentRepository.findParentCommentsByDeliveryId(deliveryId);
            commentCount = commentRepository.countByDeliveryId(deliveryId);
            likes = likeRepository
                    .findAllByUserAndCommentIn(
                            user.getId(),
                            commentRepository.findAllByDeliveryId(deliveryId)
                                    .stream().map(Comment::getId)
                                    .collect(Collectors.toList())
                    );
        } else if (postId != null) {
            // 게시글 존재 확인
            postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
            comments = commentRepository.findParentCommentsByPostId(postId);
            commentCount = commentRepository.countByPostId(postId);
            likes = likeRepository
                    .findAllByUserAndCommentIn(
                            user.getId(),
                            commentRepository.findAllByPostId(postId)
                                    .stream().map(Comment::getId)
                                    .collect(Collectors.toList())
                    );
        } else {
            throw new CustomException(COMMENT_TARGET_NOT_SELECTED);
        }

        return CommentDto.ListResponse.from(commentCount, comments, likes, user);
    }

    public void updateComment(Long userId, Long commentId, CommentDto.UpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        // 본인 댓글인지 확인
        if(!comment.getUser().getId().equals(user.getId())){
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        comment.update(request.getContent());
    }

    public void deleteComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        // 좋아요 삭제
        likeRepository.deleteByCommentId(commentId);

        // 본인 댓글인지 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        // 대댓글이 있는 상위 댓글이면 내용만 삭제됨으로 변경, 하위 댓글이면 삭제
        if(commentRepository.existsByParentCommentId(commentId)){
            comment.deleteWithChildComment();
        } else {
            Comment parentComment = comment.getParentComment();
            commentRepository.delete(comment);

            // 하위 댓글 삭제 시 더이상 상위 댓글이 존재하지 않으면 상위 댓글도 완전히 삭제
            if (parentComment != null
                    && !commentRepository.existsByParentCommentId(parentComment.getId())
                    && parentComment.getDeleted()
            ) {
                commentRepository.delete(parentComment);
            }
        }
    }
}
