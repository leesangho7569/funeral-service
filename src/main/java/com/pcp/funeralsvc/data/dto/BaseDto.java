package com.pcp.funeralsvc.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDto {
    public Long uid;
}
