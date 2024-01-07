package com.duktown.domain.comment.dto;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.post.entity.Post;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.user.entity.User;
import com.duktown.global.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateRequest{
        private Long deliveryId;
        private Long postId;
        private Long parentCommentId;
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        private String content;

        public Comment toEntity(User user, Delivery delivery, Post post, Comment parentComment) {
            return Comment.builder()
                    .user(user)
                    .delivery(delivery)
                    .post(post)
                    .parentComment(parentComment)
                    .content(content)
                    .deleted(false)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ListResponse {
        private Long commentCount;
        private List<ParentResponse> content;

        public static ListResponse from(Long commentCount, List<Comment> comments, List<Like> likes, User user){
            List<ParentResponse> content = comments.stream()
                    .map(c -> ParentResponse.from(c, likes, user, user.getId().equals(c.getUser().getId())))
                    .collect(Collectors.toList());
            return ListResponse.builder()
                    .commentCount(commentCount)
                    .content(content)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ParentResponse{
        private Long commentId;
        private Long userId;
        private String content;
        private Boolean liked;
        private Integer likeCount;
        private String dateTime;
        private Boolean deleted;
        private Boolean isWriter;
        private List<ChildResponse> childComments;

        public static ParentResponse from(Comment comment, List<Like> likes, User user, Boolean isWriter) {
            List<ChildResponse> childComments = comment.getChildComments().stream()
                    .map(c -> ChildResponse.from(c, likes, user.getId().equals(c.getUser().getId())))
                    .collect(Collectors.toList());
            return ParentResponse.builder()
                    .commentId(comment.getId())
                    .userId(comment.getUser().getId())
                    .content(comment.getContent())
                    .liked(likes.stream().anyMatch(l -> l.getComment().getId().equals(comment.getId())))
                    .likeCount(comment.getLikes().size())
                    .dateTime(DateUtil.convert(comment.getCreatedAt()))
                    .deleted(comment.getDeleted())
                    .childComments(childComments)
                    .isWriter(isWriter)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class ChildResponse {
        private Long commentId;
        private Long userId;
        private String content;
        private Boolean liked;
        private Integer likeCount;
        private Boolean isWriter;
        private String dateTime;

        public static ChildResponse from(Comment comment, List<Like> likes, Boolean isWriter) {
            return ChildResponse.builder()
                    .commentId(comment.getId())
                    .userId(comment.getUser().getId())
                    .content(comment.getContent())
                    .liked(likes.stream().anyMatch(l -> l.getComment().getId().equals(comment.getId())))
                    .likeCount(comment.getLikes().size())
                    .dateTime(DateUtil.convert(comment.getCreatedAt()))
                    .isWriter(isWriter)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "댓글의 내용은 필수 값입니다.")
        private String content;
    }
}
