package com.unishare.unishare.dtos.listing;

import com.unishare.unishare.dtos.user.UserDto;
import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import com.unishare.unishare.enums.ListingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ListingDto {

    private Long id;
    private String title;
    private String description;
    private BigDecimal pricePerDay;
    private ListingCategory category;
    private ItemCondition condition;
    private ListingStatus status;
    private LocalDateTime createdAt;
    private UserDto owner;

}
