package com.pcp.funeralsvc.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritePlaceResponseDto extends PlaceDetailResponseDto{

    private Boolean favorite;

    public FavoritePlaceResponseDto(Long placeId, String placeName, String address, String regionName, Boolean favorite) {
        super(placeId, placeName, address, regionName);

        this.favorite = favorite;
    }
}
