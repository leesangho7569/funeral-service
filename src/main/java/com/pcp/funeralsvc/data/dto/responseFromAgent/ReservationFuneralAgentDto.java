package com.pcp.funeralsvc.data.dto.responseFromAgent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pcp.funeralsvc.data.dto.BaseDto;
import com.pcp.funeralsvc.data.dto.FuneralAgentRequestEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationFuneralAgentDto extends BaseDto {

    private String translatedCertificateNo;
    private String funeralPlace;
    private String funeralDate;

}
