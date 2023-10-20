package com.duktown.domain.like.service;

import com.duktown.domain.comment.entity.Comment;
import com.duktown.domain.comment.entity.CommentRepository;
import com.duktown.domain.daily.entity.Daily;
import com.duktown.domain.daily.entity.DailyRepository;
import com.duktown.domain.delivery.entity.Delivery;
import com.duktown.domain.delivery.entity.DeliveryRepository;
import com.duktown.domain.like.dto.LikeDto;
import com.duktown.domain.like.entity.Like;
import com.duktown.domain.like.entity.LikeRepository;
import com.duktown.domain.market.entity.Market;
import com.duktown.domain.market.entity.MarketRepository;
import com.duktown.domain.user.entity.User;
import com.duktown.domain.user.entity.UserRepository;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Stream;

import static com.duktown.global.exception.CustomErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final DailyRepository dailyRepository;
    private final MarketRepository marketRepository;
    private final CommentRepository commentRepository;

    // 좋아요 추가, 취소
    public LikeDto.LikeResponse like(Long userId, LikeDto.LikeRequest request) {
        // null이 아닌 필드가 여러 개일 때 -> 잘못된 좋아요 요청
        if (Stream.of(
                        request.getDeliveryId(),
                        request.getDailyId(),
                        request.getMarketId(),
                        request.getCommentId())
                .filter(Objects::nonNull)
                .count() >= 2) {
            throw new CustomException(LIKE_TARGET_ERROR);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 좋아요 존재 시 취소
        Like findLike;

        if(request.getDeliveryId() != null) {
            findLike = likeRepository.findByUserIdAndDeliveryId(userId, request.getDeliveryId());
        } else if (request.getDailyId() != null) {
            findLike = likeRepository.findByUserIdAndDailyId(userId, request.getDailyId());
        } else if (request.getMarketId() != null) {
            findLike = likeRepository.findByUserIdAndMarketId(userId, request.getMarketId());
        } else if (request.getCommentId() != null) {
            findLike = likeRepository.findByUserIdAndCommentId(userId, request.getCommentId());
        } else {
            throw new CustomException(LIKE_TARGET_NOT_SELECTED);
        }

        if (findLike != null) {
            likeRepository.delete(findLike);
            return new LikeDto.LikeResponse(false);
        }

        // 좋아요 존재하지 않으면 추가
        Delivery delivery = null;
        Daily daily = null;
        Market market = null;
        Comment comment = null;

        if (request.getDeliveryId() != null) {
            delivery = deliveryRepository.findById(request.getDeliveryId())
                    .orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));
        } else if (request.getDailyId() != null) {
            daily = dailyRepository.findById(request.getDailyId())
                    .orElseThrow(() -> new CustomException(DAILY_NOT_FOUND));
        } else if (request.getMarketId() != null) {
            market = marketRepository.findById(request.getMarketId())
                    .orElseThrow(() -> new CustomException(MARKET_NOT_FOUND));
        } else if (request.getCommentId() != null) {
            comment = commentRepository.findById(request.getCommentId())
                    .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
            if(comment.getDeleted()) {
                throw new CustomException(COMMENT_NOT_FOUND);
            }
        } else {
            throw new CustomException(LIKE_TARGET_NOT_SELECTED);
        }

        Like like = LikeDto.LikeRequest.toEntity(user, delivery, daily, market, comment);
        likeRepository.save(like);

        return new LikeDto.LikeResponse(true);
    }
}
