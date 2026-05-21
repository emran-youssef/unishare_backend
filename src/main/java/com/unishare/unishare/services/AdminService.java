package com.unishare.unishare.services;

import com.unishare.unishare.dtos.admin.AdminStatsDto;
import com.unishare.unishare.dtos.booking.BookingDto;
import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.dtos.user.UserDto;
import com.unishare.unishare.enums.BookingStatus;
import com.unishare.unishare.enums.ListingStatus;
import com.unishare.unishare.enums.Role;
import com.unishare.unishare.exceptions.Listing.ListingNotFoundException;
import com.unishare.unishare.exceptions.UnauthorizedException.UnauthorizedActionException;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.mappers.BookingMapper;
import com.unishare.unishare.mappers.ListingMapper;
import com.unishare.unishare.mappers.UserMapper;
import com.unishare.unishare.repositories.BookingRepository;
import com.unishare.unishare.repositories.ListingRepository;
import com.unishare.unishare.repositories.PaymentRepository;
import com.unishare.unishare.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

    private UserRepository userRepository;
    private ListingRepository listingRepository;
    private BookingRepository bookingRepository;
    private PaymentRepository paymentRepository;
    private UserMapper userMapper;
    private ListingMapper listingMapper;
    private BookingMapper bookingMapper;


    public AdminStatsDto getStats(){
        return AdminStatsDto.builder()
                .totalUsers(userRepository.count())
                .activeListings(listingRepository.count())
                .totalBookings(bookingRepository.count())
                .activeListings(listingRepository.countByStatus(ListingStatus.AVAILABLE))
                .pendingBookings(bookingRepository.countByStatus(BookingStatus.PENDING))
                .completedBookings(bookingRepository.countByStatus(BookingStatus.COMPLETED))
                .totalRevenue(paymentRepository.sumPaidAmount())
                .build();

    }

    public Page<UserDto> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(user -> userMapper.toDto(user));
    }

    public UserDto getUserById(Long id){
        return userRepository.findById(id)
                .map(user -> userMapper.toDto(user))
                .orElseThrow(()->new UserNotFoundException("User not found"));
    }

    public UserDto changeUserRole(Long adminId, Long targetUserId, Role newRole){

        if(adminId.equals(targetUserId))
            throw new UnauthorizedActionException("You cannot change your own role");

        var user = userRepository.findById(targetUserId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto deactivateUser(Long adminId, Long userId){

        if(adminId.equals(userId))
            throw new UnauthorizedActionException("You cannot deactivate your own account");

        var user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found"));

        user.setIsActive(false);
        return userMapper.toDto(userRepository.save(user));

    }

    public UserDto activateUser(Long id){

        var user  = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        user.setIsActive(true);
        return userMapper.toDto(userRepository.save(user));
    }

    public Page<ListingDto> getAllListings(Pageable pageable){
        return listingRepository.findAll(pageable)
                .map(listing -> listingMapper.toDto(listing));
    }

    public ListingDto deactivateListing(Long listingId){
        var listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing not found"));

        listing.setStatus(ListingStatus.INACTIVE);
        return listingMapper.toDto(listingRepository.save(listing));

    }

    public ListingDto activateListing(Long listingId) {
        var listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing not found"));

        listing.setStatus(ListingStatus.AVAILABLE);
        return listingMapper.toDto(listingRepository.save(listing));
    }

    public Page<BookingDto> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(bookingMapper::toBookingDto);
    }

}
