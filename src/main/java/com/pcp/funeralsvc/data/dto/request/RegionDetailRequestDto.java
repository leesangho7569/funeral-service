package com.pcp.funeralsvc.data.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDetailRequestDto extends RegionRequestDto{

    @NotNull(message = "regionId cannot be null")
    private Long regionId;
}
