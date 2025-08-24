package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor
public class ReviewMapper {

    public static Review mapToReview(CreateReviewRequest request) {
        return Review.builder()
                .content(request.getContent())
                .isPositive(request.getIsPositive())
                .filmId(request.getFilmId())
                .userId(request.getUserId())
                .build();
    }

    public static ReviewDTO mapToReviewResponse(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .filmId(review.getFilmId())
                .userId(review.getUserId())
                .useful(review.getUseful() != null ? review.getUseful() : 0)
                .build();
    }

    public static Review updateReviewFields(Review review, UpdateReviewRequest request) {
        review.setIsPositive(request.getIsPositive());

        if (request.getContent() != null) {
            review.setContent(request.getContent());
        }

        return review;
    }
}
