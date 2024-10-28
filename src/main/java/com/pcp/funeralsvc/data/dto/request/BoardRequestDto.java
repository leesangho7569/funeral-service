package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pcp.funeralsvc.data.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardRequestDto extends BaseDto {

    @NotNull(message = "title cannot be null")
    @Size(min = 5, max = 100, message = "title must be between 5 and 100 characters")
    public String title;

    @NotNull(message = "content cannot be null")
    @Size(min = 10, max = 1000, message = "content must be between 10 and 1000 characters")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\p{Punct} ]*$", message = "content can only contain alphanumeric characters, spaces, Korean characters, and special symbols")
    public String content;
}
