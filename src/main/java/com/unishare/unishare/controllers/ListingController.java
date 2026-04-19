package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.listing.CreateListingRequest;
import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.dtos.listing.UpdateListingRequest;
import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import com.unishare.unishare.enums.ListingStatus;
import com.unishare.unishare.services.ListingService;
import com.unishare.unishare.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/listing")
public class ListingController {
    private final ListingService listingService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ListingDto> createListing(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateListingRequest request)
    {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(listingService.createListing(userId, request));
    }


    @GetMapping()
    @Transactional
    public ResponseEntity<Page<ListingDto>> getAllListings(
            @RequestParam(required = false) ListingCategory category,
            @RequestParam(required = false) ItemCondition condition,
            @RequestParam(required = false) ListingStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable)
    {
        return ResponseEntity.ok(
                listingService.getAllListings(category, condition, status, minPrice, maxPrice, pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<ListingDto> updateListing(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateListingRequest request)
    {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(listingService.updateListing(id, userId ,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getIdByEmail(userDetails.getUsername());
        listingService.deleteListing(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<ListingDto>> getListingsByUser(
            @PathVariable Long id,
            Pageable pageable) {
        return ResponseEntity.ok(listingService.getListingsByUser(id, pageable));
    }
}



