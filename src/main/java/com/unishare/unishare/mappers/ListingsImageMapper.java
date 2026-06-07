package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.listing.ListingImageDto;
import com.unishare.unishare.entities.ListingImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListingsImageMapper {

    ListingImageDto toDto(ListingImage listingImage);
}
