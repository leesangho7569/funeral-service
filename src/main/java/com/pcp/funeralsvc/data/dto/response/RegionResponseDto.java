package com.pcp.funeralsvc.data.dto.response;

import com.pcp.funeralsvc.data.entity.Region;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegionResponseDto {
    private Long regionId;
    private String regionName;

    public RegionResponseDto(Region region){
        this.regionId = region.getRegionId();
        this.regionName = region.getRegionName();
    }
}
