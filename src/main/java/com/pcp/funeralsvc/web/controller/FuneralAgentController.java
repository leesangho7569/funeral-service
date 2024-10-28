package com.pcp.funeralsvc.web.controller;

import com.pcp.funeralsvc.data.dto.responseFromAgent.ReservationFuneralAgentDto;
import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.ipc.logAndEvent.EventSendUtil;
import com.pcp.funeralsvc.service.FuneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pcp/funeral/v1")
public class FuneralAgentController {

    private FuneralService funeralService;

    @Autowired
    public FuneralAgentController(FuneralService funeralService){
        this.funeralService = funeralService;
    }

    @RequestMapping(value = "/customer/reservation-confirm", method = RequestMethod.POST)
    public Response reservationConfirm(@RequestBody ReservationFuneralAgentDto reservationFuneralAgentDto, @RequestHeader("Authorization") String authHeader) throws Exception{

        Response response = funeralService.reservationConfirm(reservationFuneralAgentDto);
//        LoggingContext.customer().id(Long.toString(reservationFuneralAgentDto.getUid()));
//        Map<String, Object> customData = new HashMap<>();
//        customData.put("translatedCertificateNo", reservationFuneralAgentDto.getTranslatedCertificateNo());
//        LoggingContext.sendCustomerEvent(response, "장묘 상담 요청", "customer.funeral.outbound-requested", customData);

        EventSendUtil.sendCustomerEvent(reservationFuneralAgentDto.getUid(), reservationFuneralAgentDto, response, "장묘 서비스 예약 확정", "customer.funeral.confirmed");
        return response;
    }

    // "2. 장묘 서비스 예약 수정"
    @RequestMapping(value = "/customer/reservation-update", method = RequestMethod.POST)
    public Response reservationUpdate(@RequestBody ReservationFuneralAgentDto reservationFuneralAgentDto, @RequestHeader("Authorization") String authHeader) throws Exception{

        Response response = funeralService.reservationUpdate(reservationFuneralAgentDto);
        EventSendUtil.sendCustomerEvent(reservationFuneralAgentDto.getUid(), reservationFuneralAgentDto, response, "장묘 서비스 예약 수정", "customer.funeral.reservation-updated");
        return response;
    }

    // "3. 장묘 서비스 예약 취소", description = "상조사 연동 장묘 서비스 예약 취소 api"
    @RequestMapping(value = "/customer/reservation-cancel", method = RequestMethod.POST)
    public Response reservationCancel(@RequestBody ReservationFuneralAgentDto reservationFuneralAgentDto, @RequestHeader("Authorization") String authHeader) throws Exception{

        Response response = funeralService.reservationCancel(reservationFuneralAgentDto);
        EventSendUtil.sendCustomerEvent(reservationFuneralAgentDto.getUid(), reservationFuneralAgentDto, response, "장묘 서비스 예약 취소", "customer.funeral.reservation-cancel");
        return response;
    }

    // "4. 장묘 서비스 이용 완료", description = "상조사 연동 장묘 서비스 이용 완료 api"
    @RequestMapping(value = "/customer/funeral-complete", method = RequestMethod.POST)
    public Response funeralComplete(@RequestBody ReservationFuneralAgentDto reservationFuneralAgentDto, @RequestHeader("Authorization") String authHeader) throws Exception{
        Response response = funeralService.reservationFuneralComplete(reservationFuneralAgentDto);
        EventSendUtil.sendCustomerEvent(reservationFuneralAgentDto.getUid(), reservationFuneralAgentDto, response, "장묘 서비스 이용 완료", "customer.funeral.complete");
        return response;

    }


}
