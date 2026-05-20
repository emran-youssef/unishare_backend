package com.unishare.unishare.dtos.listing;

import lombok.Data;

@Data
public class ListingImageDto {

    private Long id;
    private String imageUrl;
    private Integer displayOrder;

}
