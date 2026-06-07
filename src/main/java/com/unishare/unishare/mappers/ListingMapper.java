package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.entities.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ListingsImageMapper.class})
public interface ListingMapper {

    @Mapping(source = "listingImages", target = "images")
    ListingDto toDto(Listing listing);
}
