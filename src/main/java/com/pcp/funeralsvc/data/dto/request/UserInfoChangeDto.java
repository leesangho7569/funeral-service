package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoChangeDto {

    private String funeralCertificateNo;
    private String oldMobile;
    private String newMobile;
    private String oldAddress;
    private String newAddress;
}
