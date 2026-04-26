package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.Review;
import com.unishare.unishare.enums.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //check all reviews for a listing, used on the listing details page
    List<Review> findByListingId(Long listingId);

    // fetch all reviews received by a user — used on the user profile page
    List<Review> findByRevieweeId(Long revieweeId);

    // check if a review of this type already exists for this booking — prevents duplicate reviews
    List<Review> findByBookingIdAndType(Long bookingId, ReviewType type);

    // calculate the average star rating for a listing — shown on listing cards and detail page
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.listing.id = :listingId")
    Optional<Double> findAvgRatingByListingId(Long ListingId);
}
