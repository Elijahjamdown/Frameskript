package com.frameskipt.frameskipt.repository;

import com.frameskipt.frameskipt.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(Long userId);
    List<Review> findByGameId(Long gameId);
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
    List<Review> findTop10ByOrderByLoggedAtDesc();
}