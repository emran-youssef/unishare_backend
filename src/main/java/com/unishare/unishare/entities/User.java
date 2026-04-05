package com.unishare.unishare.entities;

import com.unishare.unishare.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "university_email", nullable = false, unique = true, length = 150)
    private String universityEmail;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // User-listing relationship: One user owns many listings.
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Listing> listings = new ArrayList<>();

    // User-Booking relationship: One user can make many bookings.
    @OneToMany(mappedBy = "renter", fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    private List<Review> reviewsGiven = new ArrayList<>();

    @OneToMany(mappedBy = "reviewee", fetch = FetchType.LAZY)
    private List<Review> reviewsReceived = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



}
