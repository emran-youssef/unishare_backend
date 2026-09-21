package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.Listing;
import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import com.unishare.unishare.enums.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing,Long> {

    // owner is the object field — Spring Data traverses owner.id via underscore
    @EntityGraph(attributePaths = "owner")
    Page<Listing> findByOwner_Id(Long ownerId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "owner")
    Page<Listing> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    @Query("""
        SELECT l FROM Listing l
        WHERE (:category IS NULL OR l.category = :category)
          AND (:condition IS NULL OR l.condition = :condition)
          AND (:status   IS NULL OR l.status   = :status)
          AND (:minPrice IS NULL OR l.pricePerDay >= :minPrice)
          AND (:maxPrice IS NULL OR l.pricePerDay <= :maxPrice)
          AND (:keyword  IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                               OR LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<Listing> searchListings(
            @Param("category") ListingCategory category,
            @Param("condition") ItemCondition condition,
            @Param("status") ListingStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // every listing the user has sent or received a chat message about — owner is fetched in the same query
    @EntityGraph(attributePaths = "owner")
    @Query("""
        SELECT l FROM Listing l
        WHERE l.id IN (
            SELECT m.listing.id FROM ChatMessage m
            WHERE m.sender.id = :userId OR m.reciever.id = :userId
        )
    """)
    List<Listing> findConversationListingsByUserId(@Param("userId") Long userId);

    long countByStatus(ListingStatus status);


}
