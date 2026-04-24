package com.unishare.unishare.entities;

import com.unishare.unishare.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //Booking-listing Relationship: One listing can have many bookings.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id")
    private Listing listing;

    // Booking-Payment Relationship: one booking has exactly one payment
    @OneToOne(mappedBy = "booking")
    private Payment payment;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    //One meetup location can be used in many bookings.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetup_location_id")
    private MeetUpLocation meetupLocation;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Booking-User Relationship: each booking belong to one user.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
