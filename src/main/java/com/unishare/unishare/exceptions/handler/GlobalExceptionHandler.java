package com.unishare.unishare.exceptions.handler;

import com.unishare.unishare.dtos.auth.ErrorResponse;
import com.unishare.unishare.exceptions.Booking.BookingNotFoundException;
import com.unishare.unishare.exceptions.Booking.BookingOverlapException;
import com.unishare.unishare.exceptions.Listing.ListingNotFoundException;
import com.unishare.unishare.exceptions.Payment.PaymentAlreadyExistsException;
import com.unishare.unishare.exceptions.Review.ReviewNotAllowedException;
import com.unishare.unishare.exceptions.UnauthorizedException.UnauthorizedActionException;
import com.unishare.unishare.exceptions.User.EmailAlreadyExistsException;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 — Validation failures (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("Bad Request")
                        .message("Validation failed")
                        .path(request.getRequestURI())
                        .fieldErrors(fieldErrors)
                        .build()
        );
    }

    // 400 — Illegal argument (end date before start date)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(400)
                        .error("Bad Request")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
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

    // 401 — Wrong credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(401)
                        .error("Unauthorized")
                        .message("Invalid university email or password")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // 403 — Unauthorized action
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAction(
            UnauthorizedActionException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(403)
                        .error("Forbidden")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    // 404 — User not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
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

    // 404 — Listing not found
    @ExceptionHandler(ListingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleListingNotFound(
            ListingNotFoundException ex,
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

    // 404 — Booking not found
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

    // 409 — Email already exists
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
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

    // 409 — Booking overlap
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

    // 500 — Generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .error("Internal Server Error")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePaymentAlreadyExists(
            PaymentAlreadyExistsException ex,
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

    @ExceptionHandler(ReviewNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotAllowedException(
            HttpServletRequest request,
            ReviewNotAllowedException ex){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(422)
                        .error("Unprocessable Entity")
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
                );


    }

}