package com.unishare.unishare.dtos.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminStatsDto {

    private Long totalUsers;
    private Long totalListing;
    private Long totalBookings;
    private Long activeListings;
    private Long pendingBookings;
    private Long completedBookings;
    private BigDecimal totalRevenue;

}
