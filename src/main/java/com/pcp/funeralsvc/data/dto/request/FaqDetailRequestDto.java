package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaqDetailRequestDto extends FaqRequestDto{

    private Long faqId;
}
