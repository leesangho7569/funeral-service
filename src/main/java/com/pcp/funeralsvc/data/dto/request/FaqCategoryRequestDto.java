package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pcp.funeralsvc.data.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaqCategoryRequestDto extends BaseDto {

    @NotNull(message = "categoryName cannot be null")
    public String categoryName;
}
