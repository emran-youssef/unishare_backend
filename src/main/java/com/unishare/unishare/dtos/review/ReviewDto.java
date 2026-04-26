package com.unishare.unishare.dtos.review;

import com.unishare.unishare.dtos.user.PublicUserDto;
import com.unishare.unishare.enums.ReviewType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private long id;
    private long bookingId;
    private PublicUserDto reviewer;
    private PublicUserDto reviewee;
    private Long listingId;
    private Integer rating;
    private String comment;
    private ReviewType type;
    private LocalDateTime createdAt;

}
