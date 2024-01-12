package com.duktown.domain.post.service;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.comment.entity.CommentRepository;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.like.entity.LikeRepository;
import com.duktown.domain.post.dto.PostDto;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.post.entity.PostRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    private final Post1PageCache post1PageCache;

    // 생성
    public void createPost(Long userId, PostDto.PostRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));

        Category category = Arrays.stream(Category.values())
                .filter(c -> c.getValue() == request.getCategory())
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

        Post post = request.toEntity(user, category);
        postRepository.save(post);
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public PostDto.PostResponse getPost(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        List<Like> likes = likeRepository.findAllByUserAndPost(user, post);
        Boolean isWriter = post.getUser().getId().equals(userId);
        return new PostDto.PostResponse(post, likes, commentRepository.countByPostId(post.getId()), isWriter);
    }

    // 목록 조회
    @Transactional(readOnly = true)
    public PostDto.PostListResponse getPostList(Long userId, Integer category, int pageNo) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Category findCategory = Arrays.stream(Category.values())
                .filter(c -> c.getValue() == category)
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

        Slice<Post> posts;
        if(pageNo == 1 && findCategory.equals(Category.DAILY)){
            posts = post1PageCache.getPage1();
        }else {
            posts = postRepository.findAllByCategory(findCategory);
        }

        List<Like> likes = likeRepository
                .findAllByUserAndPostIn(
                        user.getId(),
                        posts.stream().map(Post::getId)
                                .collect(Collectors.toList())
                );

        List<PostDto.PostResponse> postListResponses = posts.stream()
                .map(p -> new PostDto.PostResponse(p, likes, commentRepository.countByPostId(p.getId()), p.getUser().getId().equals(userId)))
                .collect(Collectors.toList());

        return new PostDto.PostListResponse(postListResponses);
    }

    public void updatePost(Long userId, Long id, PostDto.PostRequest request){
        Post updatePost = postRepository.findById(id).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        // 게시글을 작성한 유저인지 확인
        if (!updatePost.getUser().getId().equals(user.getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        updatePost.update(request.getTitle(),request.getContent());
        postRepository.save(updatePost);
    }

    public void deletePost(Long userId, Long postId) {
        Post deletePost = postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 작성한 유저인지 체크
        if (!user.getId().equals(deletePost.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        // 좋아요 먼저 삭제
        likeRepository.deleteByPostId(deletePost.getId());

        // 댓글 먼저 삭제
        List<Comment> comments = commentRepository.findAllByPostId(deletePost.getId());
        if (comments != null) {
            List<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
            likeRepository.deleteByCommentIn(commentIds);
            commentRepository.deleteAllById(commentIds);
        }

        // 게시글 삭제
        postRepository.delete(deletePost);
    }

    // 게시글 검색
    @Transactional(readOnly = true)
    public PostDto.PostListResponse searchPostList(Long userId, Integer category,String keyword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Category findCategory = Arrays.stream(Category.values())
                .filter(c -> c.getValue() == category)
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

        Slice<Post> posts = postRepository.findByCategoryAndKeyword(findCategory,keyword,PageRequest.of(0,7, Sort.by(Sort.Order.desc("createdAt"))));
        List<Like> likes = likeRepository
                .findAllByUserAndPostIn(
                        user.getId(),
                        posts.stream().map(Post::getId)
                                .collect(Collectors.toList())
                );

        List<PostDto.PostResponse> postListResponses = posts.stream()
                .map(p -> new PostDto.PostResponse(p, likes, commentRepository.countByPostId(p.getId()), p.getUser().getId().equals(userId)))
                .collect(Collectors.toList());

        return new PostDto.PostListResponse(postListResponses);
    }
}
