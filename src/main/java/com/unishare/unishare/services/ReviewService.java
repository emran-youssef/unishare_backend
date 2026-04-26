package com.unishare.unishare.services;

import com.unishare.unishare.dtos.review.CreateReviewRequest;
import com.unishare.unishare.dtos.review.ReviewDto;
import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.entities.Review;
import com.unishare.unishare.entities.User;
import com.unishare.unishare.enums.BookingStatus;
import com.unishare.unishare.enums.ReviewType;
import com.unishare.unishare.exceptions.Booking.BookingNotFoundException;
import com.unishare.unishare.exceptions.Review.ReviewNotAllowedException;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.mappers.ReviewMapper;
import com.unishare.unishare.repositories.BookingRepository;
import com.unishare.unishare.repositories.ReviewRepository;
import com.unishare.unishare.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;


    @Transactional
    public ReviewDto createReview(String callerEmail , CreateReviewRequest request){

        // 1. load the booking
        var booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(()-> new BookingNotFoundException(request.getBookingId()));

        // 2. Booking must be completed to make a review
        if(booking.getStatus() != BookingStatus.COMPLETED)
            throw new ReviewNotAllowedException("Can't make a review unless the booking is completed," +" current status:" + booking.getStatus());

        // 3. Caller must be a renter or owner
        boolean isRenter = booking.getRenter().getUniversityEmail().equals(callerEmail);
        boolean isOwner = booking.getListing().getOwner().getUniversityEmail().equals(callerEmail);

        if(!isRenter && !isOwner)
            throw new ReviewNotAllowedException("You are not involved in this booking");

        // 4.1 Enforce type matches caller role
        if (isRenter && request.getType() != ReviewType.RENTER_TO_OWNER)
            throw new ReviewNotAllowedException(
                    "Renters can only submit RENTER_TO_OWNER reviews");

        // 4.2 Enforce type matches caller role
        if (isOwner && request.getType() != ReviewType.OWNER_TO_RENTER)
            throw new ReviewNotAllowedException(
                    "Owners can only submit OWNER_TO_RENTER reviews");

        // 5. No duplicate review of the same type for this booking
        if (!reviewRepository.findByBookingIdAndType(request.getBookingId(), request.getType()).isEmpty())
            throw new ReviewNotAllowedException(
                    "You have already submitted a " + request.getType() + " review for this booking");

        // 6. Load the reviewer and reviewee
        var reviewer = userRepository.findByUniversityEmail(callerEmail)
                .orElseThrow(() -> new UserNotFoundException("Reviewer not found: " + callerEmail));

        var reviewee = userRepository.findById(request.getRevieweeId())
                .orElseThrow(() -> new UserNotFoundException("Reviewee not found: " + request.getRevieweeId()));

        // 7.  verifies that whoever the caller claims to be reviewing is actually one of the two people in this booking.
        boolean revieweeIsRenter = booking.getRenter().getId().equals(reviewee.getId());
        boolean revieweeIsOwner = booking.getListing().getOwner().getId().equals(reviewee.getId());

        if(!revieweeIsOwner && !revieweeIsRenter)
            throw new ReviewNotAllowedException("The reviewee is not involved in this booking");

        // 8. Reviewer and reviewee cannot be the same person
        if(reviewee.getId().equals(reviewer.getId()))
            throw new ReviewNotAllowedException("You cannot review yourself");

        // 9. call the helper to build the review
        var review = createReview(request, booking, reviewer, reviewee);

        return reviewMapper.toReviewDto(review);
    }


    public List<ReviewDto> getReviewsByListingId(Long listingId) {
        return reviewRepository.findByListingId(listingId)
                .stream()
                .map(reviewMapper::toReviewDto)
                .toList();
    }

    public List<ReviewDto> getReviewsByUser(Long userId) {
        return reviewRepository.findByRevieweeId(userId)
                .stream()
                .map(reviewMapper::toReviewDto)
                .toList();
    }

    public Double getAverageRating(Long listingId) {
        return reviewRepository.findAvgRatingByListingId(listingId)
                .orElse(0.0);
    }

    //Helper
    private Review createReview(
            CreateReviewRequest request,
            Booking booking, User reviewer, User reviewee ){

        return Review.builder()
                .booking(booking)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .listing(booking.getListing())
                .type(request.getType())
                .comment(request.getComment())
                .rating(request.getRating())
                .build();
    }

}
