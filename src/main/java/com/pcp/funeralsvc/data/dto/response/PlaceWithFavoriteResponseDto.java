package com.pcp.funeralsvc.data.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlaceWithFavoriteResponseDto {

    private Long placeId;
    private String placeName;
    private String address;
    private String regionName;
    private Boolean favorite;

    public PlaceWithFavoriteResponseDto(Long placeId, String placeName, String address, String regionName, Boolean favorite) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.address = address;
        this.regionName = regionName;
        this.favorite = favorite;
    }
}
