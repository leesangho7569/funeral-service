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
public class FaqRequestDto extends BaseDto {

    @NotNull(message = "categoryId cannot be null")
    private Long categoryId;

    @NotNull(message = "question cannot be null")
    private String question;

    @NotNull(message = "answer cannot be null")
    private String answer;
}
