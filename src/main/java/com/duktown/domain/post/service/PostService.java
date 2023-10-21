package com.duktown.domain.post.service;

import com.duktown.domain.comment.entity.CommentRepository;
import com.duktown.domain.post.dto.PostDto;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.post.entity.PostRespository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import com.duktown.global.type.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRespository postRespository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 생성
    public void createPost(Long userId, PostDto.PostRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(USER_NOT_FOUND));

        Category category = Arrays.stream(Category.values())
                .filter(c -> c.getValue() == request.getCategory())
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

        Post post = request.toEntity(user, category);
        postRespository.save(post);
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public PostDto.PostResponse getPost(Long id){
        Post post = postRespository.findById(id).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        return new PostDto.PostResponse(post);
    }

    // 목록 조회
    @Transactional(readOnly = true)
    public PostDto.PostListResponse getPostList(Integer category) {
        Category findCategory = Arrays.stream(Category.values())
                .filter(c -> c.getValue() == category)
                .findAny().orElseThrow(() -> new CustomException(INVALID_POST_CATEGORY_VALUE));

        List<Post> posts = postRespository.findAllByCategory(findCategory);
        List<PostDto.PostResponse> postListResponses = posts.stream()
                .map(PostDto.PostResponse::new)
                .collect(Collectors.toList());

        return new PostDto.PostListResponse(postListResponses);
    }

    public void updatePost(Long userId, Long id, PostDto.PostRequest request){
        Post updatePost = postRespository.findById(id).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        // 게시글을 작성한 유저인지 확인
        if (!updatePost.getUser().getId().equals(user.getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        updatePost.update(request.getTitle(),request.getContent());
        postRespository.save(updatePost);
    }

    public void deletePost(Long userId, Long id) {
        Post deletePost = postRespository.findById(id).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 작성한 유저인지 체크
        if (!user.getId().equals(deletePost.getUser().getId())) {
            throw new CustomException(HAVE_NO_PERMISSION);
        }

        // 댓글 먼저 삭제
        commentRepository.deleteAll(deletePost.getComments());

        // 게시글 삭제
        postRespository.delete(deletePost);
    }
}
