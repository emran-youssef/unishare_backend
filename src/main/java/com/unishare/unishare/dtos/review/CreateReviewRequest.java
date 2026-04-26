package com.unishare.unishare.dtos.review;

import com.unishare.unishare.enums.ReviewType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewRequest {


    @NotNull(message = "Booking ID is required.")
    private Long bookingId;

    @NotNull(message = "Reviewee ID is required.")
    private Long revieweeId;

    @NotNull(message = "rating is required.")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    private String comment;

    @NotNull(message = "Review type is required")
    private ReviewType type;


}
