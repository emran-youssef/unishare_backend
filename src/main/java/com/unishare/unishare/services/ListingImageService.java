package com.unishare.unishare.services;

import com.unishare.unishare.dtos.listing.ListingImageDto;
import com.unishare.unishare.entities.ListingImage;
import com.unishare.unishare.exceptions.Listing.ListingNotFoundException;
import com.unishare.unishare.exceptions.UnauthorizedException.UnauthorizedActionException;
import com.unishare.unishare.repositories.ListingImageRepository;
import com.unishare.unishare.repositories.ListingRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ListingImageService {

    private final ListingRepository listingRepository;
    private final ListingImageRepository listingImageRepository;
    private final String uploadDir;

    public ListingImageService(
            ListingRepository listingRepository,
            ListingImageRepository listingImageRepository,
            @Value("${file.upload-dir}") String uploadDir
    ) {
        this.listingRepository = listingRepository;
        this.listingImageRepository = listingImageRepository;
        this.uploadDir = uploadDir;
    }

    public List<ListingImageDto> uploadImage(
            Long listingId,
            Long requestingUserId,
            List<MultipartFile> files) {

        var listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing not found"));

        if (!listing.getOwner().getId().equals(requestingUserId))
            throw new UnauthorizedActionException("Only the listing owner can upload images");

        // 3. Create the directory for this listing if it doesn't exist yet
        Path listingDir = Paths.get(uploadDir, "listings", listingId.toString());
        try {
            Files.createDirectories(listingDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        // 4. Count existing images — drives display_order for the new batch
        int existingCount = listingImageRepository.countByListingId(listingId);

        List<ListingImageDto> saved = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            // 5. Reject non-image files before touching the disk
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/"))
                throw new IllegalArgumentException("Only image files are allowed. Rejected: " + file.getOriginalFilename());

            // 6. UUID filename — prevents collisions and path traversal
            String extension = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;
            Path filePath = listingDir.resolve(filename);

            try {
                Files.write(filePath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file: " + filename, e);
            }

            // 7. Build the public URL and persist the ListingImage record
            String imageUrl = "/uploads/listings/" + listingId + "/" + filename;

            var image = ListingImage.builder()
                    .listing(listing)
                    .imageUrl(imageUrl)
                    .displayOrder(existingCount + i)
                    .build();

            var savedImage = listingImageRepository.save(image);

            ListingImageDto dto = new ListingImageDto();
            dto.setId(savedImage.getId());
            dto.setImageUrl(savedImage.getImageUrl());
            dto.setDisplayOrder(savedImage.getDisplayOrder());
            saved.add(dto);
        }

        return saved;
    }

    public List<ListingImageDto> getImagesByListing(Long listingId) {
        return listingImageRepository.findByListingIdOrderByDisplayOrder(listingId)
                .stream()
                .map(img -> {
                    ListingImageDto dto = new ListingImageDto();
                    dto.setId(img.getId());
                    dto.setImageUrl(img.getImageUrl());
                    dto.setDisplayOrder(img.getDisplayOrder());
                    return dto;
                })
                .toList();
    }


    private String getExtension(String filename){
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

}
