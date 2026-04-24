package com.unishare.unishare.services;

import com.unishare.unishare.dtos.booking.BookingDto;
import com.unishare.unishare.dtos.booking.CreateBookingRequest;
import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.enums.BookingStatus;
import com.unishare.unishare.exceptions.Booking.BookingNotFoundException;
import com.unishare.unishare.exceptions.Booking.BookingOverlapException;
import com.unishare.unishare.exceptions.Listing.ListingNotFoundException;
import com.unishare.unishare.exceptions.UnauthorizedException.UnauthorizedActionException;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.mappers.BookingMapper;
import com.unishare.unishare.repositories.BookingRepository;
import com.unishare.unishare.repositories.ListingRepository;
import com.unishare.unishare.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;


    //create booking
    public BookingDto createBooking(Long renterId, CreateBookingRequest request)
    {
        var listing = listingRepository.findById(request.getListingId())
                .orElseThrow(()-> new ListingNotFoundException("Listing not found"));

        var renter = userRepository.findById(renterId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        // Renter cannot book their listing
        if(listing.getOwner().getId().equals(renterId))
            throw new UnauthorizedActionException("You cannot book your own listing");



        // validate date range
        if(!request.getEndDate().isAfter(request.getStartDate()))
            throw new IllegalArgumentException("End date must be after start date");


        // Overlap Detection - checking against confirmed booking only
        var overlapping = bookingRepository.findOverlappingBookings(
                request.getListingId(),
                request.getStartDate(),
                request.getEndDate(),
                BookingStatus.CONFIRMED
                );

        if(!overlapping.isEmpty())
            throw new BookingOverlapException();

        //calculate days of renting and the total price
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        BigDecimal totalPrice = listing.getPricePerDay().multiply(BigDecimal.valueOf(days));

        var booking = Booking.builder()
                .listing(listing)
                .renter(renter)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .build();

        var saved = bookingRepository.save(booking);

        return bookingMapper.toBookingDto(saved);
    }

    // read a booking
    public List<BookingDto> getMyBookings(Long renterId){
        return bookingRepository.findByRenter_Id(renterId)
                .stream()
                .map(booking -> bookingMapper.toBookingDto(booking))
                .toList();
    }

    // get booking
    public BookingDto getBookingById(Long bookingId, Long requestingUserId) {
        var booking = getBooking(bookingId);

        boolean isRenter = booking.getRenter().getId().equals(requestingUserId);
        boolean isOwner  = booking.getListing().getOwner().getId().equals(requestingUserId);

        if (!isRenter && !isOwner)
            throw new UnauthorizedActionException("You are not involved in this booking");

        return bookingMapper.toBookingDto(booking);
    }

    public BookingDto cancelBooking(Long bookinId, Long requestingUserId)
    {
        var booking = getBooking(bookinId);

        boolean isRenter = booking.getRenter().getId().equals(requestingUserId);
        boolean isOwner = booking.getListing().getOwner().getId().equals(requestingUserId);

        if(!isRenter && !isOwner)
            throw new UnauthorizedActionException("You are not involved in this booking");

        if(booking.getStatus() == BookingStatus.COMPLETED)
            throw new UnauthorizedActionException("Cannot cancel a completed booking");

        booking.setStatus(BookingStatus.CANCELLED);

        var saved = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(saved);

    }

    public BookingDto confirmBooking(Long bookingId, Long requestingUserId) {
        var booking = getBooking(bookingId);

        boolean isOwner = booking.getListing().getOwner().getId().equals(requestingUserId);
        if (!isOwner) {
            throw new UnauthorizedActionException("Only the listing owner can confirm a booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new UnauthorizedActionException("Only PENDING bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingDto completeBooking(Long bookingId, Long requestingUserId) {
        var booking = getBooking(bookingId);

        boolean isOwner = booking.getListing().getOwner().getId().equals(requestingUserId);
        if (!isOwner) {
            throw new UnauthorizedActionException("Only the listing owner can complete a booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new UnauthorizedActionException("Only CONFIRMED bookings can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }


    private Booking getBooking(long id){
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }


}
