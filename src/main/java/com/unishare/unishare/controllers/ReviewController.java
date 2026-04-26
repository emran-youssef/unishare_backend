package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.review.CreateReviewRequest;
import com.unishare.unishare.dtos.review.ReviewDto;
import com.unishare.unishare.services.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")         // Authenticated — renter or owner of a COMPLETED booking
    public ResponseEntity<ReviewDto> createReview(
           @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication){

        return ResponseEntity.status(HttpStatus.CREATED).
                body(reviewService.createReview(authentication.getName(), request));
    }


    @GetMapping("/listing/{id}")    // Public — listed in SecurityConfig permitAll
    public ResponseEntity<List<ReviewDto>> getReviewsByListing(@PathVariable Long id) {

        return ResponseEntity.ok(reviewService.getReviewsByListingId(id));
    }


    @GetMapping("/user/{id}")       // Public — any user's received reviews are visible
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(id));
    }

}
