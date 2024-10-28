package com.pcp.funeralsvc.data.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationResponseDto {
    private String funeralCertificateNo;
    private String translatedCertificateNo;
    private String placeName;
}
