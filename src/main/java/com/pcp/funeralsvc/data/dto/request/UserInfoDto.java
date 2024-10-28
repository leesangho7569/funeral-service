package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDto {

    private Long uid;
    private String customerName;
    private String mobile;
    private String passAppId;

    public UserInfoDto(Long uid, String customerName, String mobile, String passAppId) {

        this.uid = uid;
        this.customerName = customerName;
        this.mobile = mobile;
        this.passAppId = passAppId;
    }
}
