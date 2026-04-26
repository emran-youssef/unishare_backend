package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.review.ReviewDto;
import com.unishare.unishare.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReviewMapper {

    @Mapping(source = "booking.id" ,target = "bookingId")
    @Mapping(source = "listing.id", target = "listingId")
    ReviewDto toReviewDto(Review review);
}
