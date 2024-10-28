package com.pcp.funeralsvc.data.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.pcp.funeralsvc.data.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OutboundRequestDto extends BaseDto {

    @NotNull(message = "translatedCertificateNo cannot be null")
    String translatedCertificateNo;
    String funeralCertificateNo;
    String customerName;
    String mobile;
    @NotNull(message = "vehicleSvcSupport cannot be null")
    Boolean vehicleSvcSupport;
//    Long placeId;
}
