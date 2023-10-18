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
import com.duktown.global.exception.CustomErrorType;
import com.duktown.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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

    // 좋아요 추가
    public void createLike(Long userId, Long deliveryId, Long dailyId, Long marketId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Delivery delivery = null;
        Daily daily = null;
        Market market = null;
        Comment comment = null;

        if (deliveryId != null) {
            delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new CustomException(DELIVERY_NOT_FOUND));
        } else if (dailyId != null) {
            daily = dailyRepository.findById(dailyId)
                    .orElseThrow(() -> new CustomException(DAILY_NOT_FOUND));
        } else if (marketId != null) {
            market = marketRepository.findById(marketId)
                    .orElseThrow(() -> new CustomException(MARKET_NOT_FOUND));
        } else if (commentId != null) {
            comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        } else {
            throw new CustomException(LIKE_TARGET_NOT_SELECTED);
        }

        Like like = LikeDto.LikeRequest.toEntity(user, delivery, daily, market, comment);
        likeRepository.save(like);
    }
}
