package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pcp.funeralsvc.data.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceRequestDto extends BaseDto {
    private Long regionId;
    private String placeName;
    private String address;
    private String introduction;
}
