package com.unishare.unishare.dtos.listing;

import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import com.unishare.unishare.enums.ListingStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateListingRequest {

    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerDay;

    private ListingCategory category;

    private ItemCondition condition;

    private ListingStatus status;


    // al fields are optional here, the user send only what he want to change
}
