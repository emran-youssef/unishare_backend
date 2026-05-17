package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.booking.BookingDto;
import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.entities.MeetUpLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ListingMapper.class, MeetUpLocationMapper.class})
public interface BookingMapper {

    BookingDto toBookingDto(Booking booking);

}
