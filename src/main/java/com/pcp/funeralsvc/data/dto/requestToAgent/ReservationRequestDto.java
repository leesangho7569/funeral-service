package com.pcp.funeralsvc.data.dto.requestToAgent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationRequestDto {

    //상조사 증서 번호
    String translatedCertificateNo;
    //String reservationId;
    //희망 장묘지 이름
    List<String> cemeteryName;
    //계약자 명
    String custNm;
    //계약자 연락처
    String mpNo;
    Boolean vehicleSvcSupport;
}
