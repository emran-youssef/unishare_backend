package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.entities.Listing;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring", uses = {UserMapper.class})
public interface ListingMapper {

    ListingDto toDto(Listing listing);
}
