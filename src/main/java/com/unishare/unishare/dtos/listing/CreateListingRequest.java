package com.unishare.unishare.dtos.listing;

import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateListingRequest {

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "9999.99", message = "Price must be less than 10,000")
    private BigDecimal pricePerDay;

    @NotNull(message = "Category is required")
    private ListingCategory category;

    @NotNull(message = "Condition is required")
    private ItemCondition condition;
}
