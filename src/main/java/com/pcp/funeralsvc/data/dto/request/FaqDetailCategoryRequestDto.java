package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaqDetailCategoryRequestDto extends FaqCategoryRequestDto{

    @NotNull(message = "categoryId cannot be null")
    public Long categoryId;
}
