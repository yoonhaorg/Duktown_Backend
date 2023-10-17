package com.duktown.domain.comment.service;

import com.duktown.domain.comment.dto.CommentDto;
import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.comment.entity.CommentRepository;
import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.daily.entity.DailyRepository;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.market.entity.Market;
import com.duktown.domain.market.entity.MarketRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final DeliveryRepository deliveryRepository;
    private final DailyRepository dailyRepository;
    private final MarketRepository marketRepository;

    public void createComment(Long userId, CommentDto.CreateRequest request){
        // 사용자
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 부모 댓글
        Comment parentComment = null;
        if(request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new CustomException(PARENT_COMMENT_NOT_FOUND));

            // TODO: 이부분 지우고 나중에 따로 commit
            if(parentComment.getParentComment() != null){
                throw new CustomException(COMMENT_DEPTH_ERROR);
            }
        }

        Delivery delivery = null;
        Daily daily = null;
        Market market = null;
        if(request.getDeliveryId() != null){
            delivery = deliveryRepository.findById(request.getDeliveryId())
                    .orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));
        } else if (request.getDailyId() != null) {
            daily = dailyRepository.findById(request.getDailyId())
                    .orElseThrow(() -> new CustomException(DAILY_NOT_FOUND));
        } else if (request.getMarketId() != null) {
            market = marketRepository.findById(request.getMarketId())
                    .orElseThrow(() -> new CustomException(MARKET_NOT_FOUND));
        } else {
            throw new CustomException(COMMENT_TARGET_NOT_SELECTED);
        }

        Comment comment = request.toEntity(user, delivery, daily, market, parentComment);
        commentRepository.save(comment);
    }

    // TODO: queryDsl 도입해 대댓글 조회 기능 수정
    /*
    문제점 : 댓글에 대댓글을 달았을 경우 다음과 같이 조회됨
    댓글 id : 1
    ㄴ 대댓글 id : 2
    댓글 id : 2 (대댓글과 동일)
     */
    @Transactional(readOnly = true)
    public CommentDto.ListResponse getCommentList(Long userId, Long deliveryId, Long dailyId, Long marketId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<Comment> comments;

        if(deliveryId != null){
            comments = commentRepository.findAllByDeliveryId(deliveryId);
            return CommentDto.ListResponse.from(comments);
        } else if (dailyId != null) {
            comments = commentRepository.findAllByDailyId(dailyId);
        } else if (marketId != null) {
            comments = commentRepository.findAllByMarketId(marketId);
        } else {
            throw new CustomException(COMMENT_TARGET_NOT_SELECTED);
        }

        return CommentDto.ListResponse.from(comments);
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
