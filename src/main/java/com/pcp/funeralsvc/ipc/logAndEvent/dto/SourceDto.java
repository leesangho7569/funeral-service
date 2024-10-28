package com.pcp.funeralsvc.ipc.logAndEvent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드는 JSON에 포함되지 않음
public class SourceDto {
    private String service;
    private String host;
    private String ip;
}
