package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pcp.funeralsvc.data.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoritePlaceSetRequestDto extends BaseDto {

    @NotNull(message = "placeId 정보 없음")
    Long placeId;

    @NotNull(message = "favorite 설정/해제 정보 없음")
    Boolean favorite;
}
