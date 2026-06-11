package com.unishare.unishare.services;

import com.unishare.unishare.dtos.listing.CreateListingRequest;
import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.dtos.listing.UpdateListingRequest;
import com.unishare.unishare.entities.Listing;
import com.unishare.unishare.enums.BookingStatus;
import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import com.unishare.unishare.enums.ListingStatus;
import com.unishare.unishare.exceptions.Listing.ListingNotFoundException;
import com.unishare.unishare.exceptions.UnauthorizedException.UnauthorizedActionException;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.mappers.ListingMapper;
import com.unishare.unishare.repositories.BookingRepository;
import com.unishare.unishare.repositories.ListingRepository;
import com.unishare.unishare.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ListingService {

    private final UserRepository userRepository;
    private final ListingMapper listingMapper;
    private final ListingRepository listingRepository;
    private final BookingRepository bookingRepository;



    public ListingDto createListing(
            Long ownerId, CreateListingRequest request
    )
    {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        var listing = Listing.builder()
                .owner(owner)
                .title(request.getTitle())
                .description(request.getDescription())
                .pricePerDay(request.getPricePerDay())
                .category(request.getCategory())
                .condition(request.getCondition())
                .status(ListingStatus.AVAILABLE)
                .build();

        return listingMapper.toDto(listingRepository.save(listing));
    }

    @Transactional
    public Page<ListingDto> getAllListings(
            String keyword,
            ListingCategory category,
            ItemCondition condition,
            ListingStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable)
    {

        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return listingRepository
                .searchListings(category, condition, status, minPrice, maxPrice, kw, pageable)
                .map(listingMapper::toDto);
    }

    @Transactional
    public ListingDto updateListing(
            Long listingId, Long requestingUserId, UpdateListingRequest request)
    {
        var listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing not found"));

        if (!listing.getOwner().getId().equals(requestingUserId)) {
            throw new UnauthorizedActionException("You are not the owner of this listing");
        }

        if (request.getTitle() != null)       listing.setTitle(request.getTitle());
        if (request.getDescription() != null) listing.setDescription(request.getDescription());
        if (request.getPricePerDay() != null) listing.setPricePerDay(request.getPricePerDay());
        if (request.getCategory() != null)    listing.setCategory(request.getCategory());
        if (request.getCondition() != null)   listing.setCondition(request.getCondition());
        if (request.getStatus() != null)      listing.setStatus(request.getStatus());

        listingRepository.save(listing);
        var updated = listingRepository.findById(listingId).orElseThrow();
        return listingMapper.toDto(updated);
    }

    @Transactional
    public void deleteListing(Long listingId, Long requestingUserId)
    {
        var listing = findListingById(listingId);

        if (!listing.getOwner().getId().equals(requestingUserId)) {
            throw new UnauthorizedActionException("You are not the owner of this listing");
        }
        listingRepository.delete(listing);
    }

    @Transactional
    public ListingDto getListingById(Long id){
        var listing = findListingById(id);
        ListingDto dto = listingMapper.toDto(listing);

        if(listing.getStatus() == ListingStatus.RENTED) {
            bookingRepository.findByListingIdAndStatus(listing.getId(), BookingStatus.CONFIRMED)
                    .ifPresent(booking -> dto.setAvailableFrom(booking.getEndDate().toString()));
        }

        return dto;
    }

    @Transactional
    public Page<ListingDto> getListingsByUser(Long userId, Pageable pageable) {
        return listingRepository.findByOwner_Id(userId, pageable)
                .map(listingMapper::toDto);
    }

    //helper
    private Listing findListingById(Long id){
        return listingRepository.findById(id)
                .orElseThrow(()-> new ListingNotFoundException("Listing not found" + id));

    }

    }



