package com.duktown.domain.comment.dto;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.market.entity.Market;
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
        private Long dailyId;
        private Long marketId;
        private Long parentCommentId;
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        private String content;

        public Comment toEntity(User user, Delivery delivery, Daily daily, Market market, Comment parentComment) {
            return Comment.builder()
                    .user(user)
                    .delivery(delivery)
                    .daily(daily)
                    .market(market)
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
        private Integer commentCount;
        private List<ParentResponse> content;

        public static ListResponse from(List<Comment> comments){
            List<ParentResponse> content = comments.stream()
                    .map(ParentResponse::from)
                    .collect(Collectors.toList());
            return ListResponse.builder()
                    .commentCount(comments.size())
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
        private String dateTime;
        private Boolean deleted;
        private List<ChildResponse> childComments;

        public static ParentResponse from(Comment comment) {
            List<ChildResponse> childComments = comment.getChildComments().stream()
                    .map(ChildResponse::from)
                    .collect(Collectors.toList());
            return ParentResponse.builder()
                    .commentId(comment.getId())
                    .userId(comment.getUser().getId())
                    .content(comment.getContent())
                    .dateTime(DateUtil.convert(comment.getCreatedAt()))
                    .deleted(comment.getDeleted())
                    .childComments(childComments)
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
        private String dateTime;

        public static ChildResponse from(Comment comment) {
            return ChildResponse.builder()
                    .commentId(comment.getId())
                    .userId(comment.getUser().getId())
                    .content(comment.getContent())
                    .dateTime(DateUtil.convert(comment.getCreatedAt()))
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
