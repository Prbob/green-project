package com.brogrammers.brogrammers.domain.service;

import com.brogrammers.brogrammers.domain.board.Review;
import com.brogrammers.brogrammers.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository rp;

    public void save(Review review){
        rp.save(review);
    }

    public Page<Review> findReviewByProductName(String name, Pageable pageable){
        return rp.findAllByProductName(name,pageable);
    }
}
