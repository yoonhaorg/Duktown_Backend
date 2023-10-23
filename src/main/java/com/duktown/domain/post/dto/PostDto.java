package com.duktown.domain.post.dto;

import com.duktown.domain.like.entity.Like;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.Category;
import com.duktown.global.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

public class PostDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostRequest{
        @NotNull(message = "카테고리는 필수 입력값입니다.")
        @Min(value = 0, message = "카테고리는 0또는 1의 정수 값입니다.")
        @Max(value = 1, message = "카테고리는 0또는 1의 정수 값입니다.")
        private Integer category;

        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력값입니다")
        private String content;

        // DTO -> Entity
        public Post toEntity(User user, Category category){
            return Post.builder()
                    .user(user)
                    .category(category)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PostResponse{
        private Long id;
        private Long userId;
        private Integer category;
        private String title;
        private String content;
        private Boolean liked;
        private Integer likeCount;
        private String datetime;

        public PostResponse(Post post, List<Like> likes){
            this.id = post.getId();
            this.userId = post.getUser().getId();
            this.category = post.getCategory().getValue();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.liked = likes.stream().anyMatch(l -> l.getPost().getId().equals(post.getId()));
            this.likeCount = post.getLikes().size();
            this.datetime = DateUtil.convert(post.getCreatedAt());
        }
    }

    @Getter
    public static class PostListResponse{
        private List<PostResponse> content;

        public PostListResponse(List<PostResponse> content){
            this.content = content;
        }
    }

}
