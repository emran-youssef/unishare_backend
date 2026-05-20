package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingImageRepository extends JpaRepository<ListingImage, Long> {

    // fetch images for a listing ordered by display_order — used on listing detail page
    List<ListingImage> findByListingIdOrderByDisplayOrder(Long listingId);

    // count existing images — drives display_order for newly uploaded images
    int countByListingId(Long listingId);
}
