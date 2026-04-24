package com.unishare.unishare.exceptions.Booking;

import com.unishare.unishare.dtos.auth.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BookingsGlobalExceptionHandler {

    // handle booking not found exception
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFound(
            BookingNotFoundException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(404)
                        .error("Not Found")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // handel overlapping exception
    @ExceptionHandler(BookingOverlapException.class)
    public ResponseEntity<ErrorResponse> handleBookingOverlap(
            BookingOverlapException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(409)
                        .error("Conflict")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }
}
