package com.pcp.funeralsvc.ipc.logAndEvent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String id;
    private String ip;
    private String agent;
}
