package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoServiceDto extends UserInfoDto{
    String translatedCertificateNo;
    String funeralCertificateNo;

    public UserInfoServiceDto(Long uid, String customerName, String mobile, String passAppId, String translatedCertificateNo, String funeralCertificateNo) {
        super(uid, customerName, mobile, passAppId);
        this.translatedCertificateNo = translatedCertificateNo;
        this.funeralCertificateNo = funeralCertificateNo;
    }

}
