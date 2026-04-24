package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.booking.BookingDto;
import com.unishare.unishare.entities.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ListingMapper.class})
public interface BookingMapper {

    BookingDto toBookingDto(Booking booking);

}
