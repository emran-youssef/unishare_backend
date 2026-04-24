package com.unishare.unishare.dtos.booking;

import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.dtos.user.UserDto;
import com.unishare.unishare.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private ListingDto listing;
    private UserDto renter;
    private LocalDateTime createdAt;

}
