package com.unishare.unishare.dtos.listing;

import com.unishare.unishare.enums.ItemCondition;
import com.unishare.unishare.enums.ListingCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateListingRequest {

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    private String description;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerDay;

    @NotNull(message = "Category is required")
    private ListingCategory category;

    @NotNull(message = "Condition is required")
    private ItemCondition condition;
}
