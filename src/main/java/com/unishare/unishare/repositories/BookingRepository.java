package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenterId(Long renterId);

    List<Booking> findByListingId(Long listingId);

    // overlap detection:

    @Query("""
    SELECT b FROM Booking b
    WHERE b.listing.id = :listingId
      AND b.status = :status
      AND b.startDate <= :endDate
      AND b.endDate   >= :startDate
""")
    List<Booking> findOverlappingBookings(
            @Param("listingId")  Long listingId,
            @Param("startDate")  LocalDate startDate,
            @Param("endDate")    LocalDate endDate,
            @Param("status")     BookingStatus status
    );

}
