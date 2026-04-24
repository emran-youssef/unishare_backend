package com.unishare.unishare.dtos.booking;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {

    @NotNull(message = "listingId is required")
    private Long listingId;

    @NotNull(message = "Start date is required")
    @Future(message = "Start Date must be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;



}
