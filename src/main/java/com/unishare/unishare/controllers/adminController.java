package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.admin.AdminStatsDto;
import com.unishare.unishare.dtos.booking.BookingDto;
import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.dtos.user.UserDto;
import com.unishare.unishare.enums.Role;
import com.unishare.unishare.services.AdminService;
import com.unishare.unishare.services.UserService;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class adminController {

    private final AdminService adminService;
    private UserService userService;

    // ── Stats ─────────────────────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats(){
        return ResponseEntity.ok(adminService.getStats());

    }

    // ── Users ─────────────────────────────────────────────────────────────
    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(name, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDto> changeUserRole(
            @PathVariable Long id,
            @RequestParam Role role,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long adminId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(adminService.changeUserRole(adminId, id, role));
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<UserDto> deactivateUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long adminId = userService.getIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(adminService.deactivateUser(adminId, id));
    }


    @PutMapping("/users/{id}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateUser(id));
    }

    // ── Listings ─────────────────────────────────────────────────────────────

    @GetMapping("/listings")
    public ResponseEntity<Page<ListingDto>> getAllListings(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllListings(pageable));
    }

    @PutMapping("/listings/{id}/deactivate")
    public ResponseEntity<ListingDto> deactivateListing(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deactivateListing(id));
    }

    @PutMapping("/listings/{id}/activate")
    public ResponseEntity<ListingDto> activateListing(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateListing(id));
    }

    // ── Bookings ─────────────────────────────────────────────────────────────

    @GetMapping("/bookings")
    public ResponseEntity<Page<BookingDto>> getAllBookings(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllBookings(pageable));
    }

}
