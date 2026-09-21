package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // everything BookingDto needs (listing + its owner, renter, meetup location) is fetched in the same query
    @EntityGraph(attributePaths = {"listing", "listing.owner", "renter", "meetupLocation"})
    List<Booking> findByRenter_Id(Long renterId);

    // used by the admin bookings endpoint
    @Override
    @EntityGraph(attributePaths = {"listing", "listing.owner", "renter", "meetupLocation"})
    Page<Booking> findAll(Pageable pageable);

    List<Booking> findByListing_Id(Long listingId);

    // Overlap detection for date ranges where endDate is treated as the return/check-out date.
    @Query("""
    SELECT b FROM Booking b
    WHERE b.listing.id = :listingId
      AND b.status IN :statuses
      AND (:excludedBookingId IS NULL OR b.id <> :excludedBookingId)
      AND b.startDate < :endDate
      AND b.endDate   > :startDate
""")
    List<Booking> findOverlappingBookings(
            @Param("listingId")  Long listingId,
            @Param("startDate")  LocalDate startDate,
            @Param("endDate")    LocalDate endDate,
            @Param("statuses")   List<BookingStatus> statuses,
            @Param("excludedBookingId") Long excludedBookingId
    );

    long countByStatus(BookingStatus status);

    @EntityGraph(attributePaths = {"listing", "listing.owner", "renter", "meetupLocation"})
    List<Booking>  findByListing_Owner_id(Long ownerId);

    Optional<Booking> findByListingIdAndStatus(Long listingId, BookingStatus status);

}
