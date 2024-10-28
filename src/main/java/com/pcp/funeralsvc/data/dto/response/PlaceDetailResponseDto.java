package com.pcp.funeralsvc.data.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlaceDetailResponseDto {

    private Long placeId;
    private String placeName;
    private String address;
    private String regionName;

    public PlaceDetailResponseDto(Long placeId, String placeName, String address, String regionName) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.address = address;
        this.regionName = regionName;
    }
}
