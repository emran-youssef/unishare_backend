package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.booking.BookingDto;
import com.unishare.unishare.dtos.booking.CreateBookingRequest;
import com.unishare.unishare.services.BookingService;
import com.unishare.unishare.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bookings")

public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<BookingDto> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateBookingRequest request)
    {
        Long userId = userService.getIdByEmail(userDetails.getUsername());   //Username = uniEmail
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(userId, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingDto>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails)
    {
        Long userId = userService.getIdByEmail(userDetails.getUsername());    //Username = uniEmail
        return ResponseEntity.ok(bookingService.getMyBookings(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.getBookingById(id, userId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.cancelBooking(id, userId));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingDto> confirmBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.confirmBooking(id, userId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingDto> completeBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.completeBooking(id, userId));
    }


}
