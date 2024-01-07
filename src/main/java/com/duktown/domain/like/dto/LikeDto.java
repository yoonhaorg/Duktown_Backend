package com.duktown.domain.like.dto;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LikeRequest {
        private Long postId;
        private Long commentId;

        public static Like toEntity(User user, Post post, Comment comment) {
            return Like.builder()
                    .user(user)
                    .post(post)
                    .comment(comment)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class LikeResponse {
        private Boolean liked;
    }
}
