package com.pcp.funeralsvc.data.dto.responseFromAgent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFromAgentDto {

    private String responseCode;
    private String responseMessage;
}
