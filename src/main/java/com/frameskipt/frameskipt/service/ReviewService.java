package com.frameskipt.frameskipt.service;

import com.frameskipt.frameskipt.model.Review;
import com.frameskipt.frameskipt.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Persists a new review, enforcing the rule that a single user
     * cannot log more than one review for the same game.
     */
    public Review saveReview(Review review) {

        Long userId = review.getUser().getId();
        Long gameId = review.getGame().getId();

        boolean alreadyReviewed =
            reviewRepository.existsByUserIdAndGameId(userId, gameId);

        if (alreadyReviewed) {
            throw new IllegalStateException(
                "You have already logged a review for this game."
            );
        }

        return reviewRepository.save(review);
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No review found with id: " + id
            ));
    }

    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> findByGameId(Long gameId) {
        return reviewRepository.findByGameId(gameId);
    }

    public List<Review> findRecentActivity() {
        return reviewRepository.findTop10ByOrderByLoggedAtDesc();
    }

    /**
     * Calculates the average rating for a game, rounded to one
     * decimal place to respect our half-star precision (e.g. 4.3,
     * not 4.333333333).
     */
    public Double getAverageRatingForGame(Long gameId) {

        List<Review> reviews = reviewRepository.findByGameId(gameId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        double average = sum / reviews.size();

        // Round to 1 decimal place: multiply, round, divide back.
        return Math.round(average * 10.0) / 10.0;
    }
}