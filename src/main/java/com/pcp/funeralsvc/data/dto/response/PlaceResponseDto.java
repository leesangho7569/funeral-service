package com.pcp.funeralsvc.data.dto.response;

import lombok.Data;

@Data
public class PlaceResponseDto {

    private Long placeId;
    private String placeName;

    private String address;
    private String introduction;

    private String imageFile;

}
