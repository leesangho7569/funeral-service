package com.pcp.funeralsvc.data.dto;

import lombok.Getter;

@Getter
public enum FuneralAgentRequestEnum {

    SERVICE_RESERVATION_IDLE(0, "처음 상태"),
    SERVICE_RESERVATION_REQUEST(1, "장묘 서비스 예약 요청"),
    SERVICE_RESERVATION_CONFIRM(2, "장묘 서비스 예약 확정"),
    SERVICE_RESERVATION_UPDATE(3, "장묘 서비스 예약 수정"),
    SERVICE_RESERVATION_CANCEL(4, "장묘 서비스 예약 취소"),
    SERVICE_USE_COMPLETE(5, "장묘 서비스 이용 완료");

    private int code;
    private String msg;
    FuneralAgentRequestEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
