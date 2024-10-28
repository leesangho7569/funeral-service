package com.pcp.funeralsvc.ipc.logAndEvent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드는 JSON에 포함되지 않음
public class CustomerEventDto {
    private String timestamp;
    @JsonIgnore
    private SourceDto source;
    private DataDto data;
}
