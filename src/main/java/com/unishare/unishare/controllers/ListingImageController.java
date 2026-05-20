package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.listing.ListingImageDto;
import com.unishare.unishare.services.ListingImageService;
import com.unishare.unishare.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/listings/{listingId}/images")
public class ListingImageController {

    private final ListingImageService listingImageService;
    private final UserService userService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<List<ListingImageDto>> uploadImages(
            @PathVariable Long listingId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("files") List<MultipartFile> files) {

        Long userId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(listingImageService.uploadImage(listingId, userId, files));
    }

    @GetMapping
    public ResponseEntity<List<ListingImageDto>> getImages(
            @PathVariable Long listingId) {

        return ResponseEntity.ok(listingImageService.getImagesByListing(listingId));
    }
}
