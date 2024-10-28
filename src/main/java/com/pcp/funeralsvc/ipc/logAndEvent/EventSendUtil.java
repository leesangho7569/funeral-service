package com.pcp.funeralsvc.ipc.logAndEvent;

import com.pcp.funeralsvc.data.dto.request.OutboundRequestDto;
import com.pcp.funeralsvc.data.dto.request.PlaceDetailRequestDto;
import com.pcp.funeralsvc.data.dto.request.PlaceRequestDto;
import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.data.dto.responseFromAgent.ReservationFuneralAgentDto;
import com.pcp.funeralsvc.ipc.logAndEvent.message.LoggingContext;

import java.util.HashMap;
import java.util.Map;

public class EventSendUtil {

    public static <T> void sendCustomerEvent(Long uid, T requestDto, Response response, String topicName, String topic){

        LoggingContext.customer().id(Long.toString(uid));
        Map<String, Object> customData = new HashMap<>();

        if(requestDto instanceof PlaceRequestDto){
            customData.put("장묘지 이름", ((PlaceRequestDto) requestDto).getPlaceName());
            customData.put("주소", ((PlaceRequestDto) requestDto).getAddress());
        } else if(requestDto instanceof PlaceDetailRequestDto){
            customData.put("placeId", ((PlaceDetailRequestDto) requestDto).getPlaceId());
            customData.put("장묘지 이름", ((PlaceDetailRequestDto) requestDto).getPlaceName());

        } else if(requestDto instanceof OutboundRequestDto) {
            customData.put("translatedCertificateNo", ((OutboundRequestDto) requestDto).getTranslatedCertificateNo());
        } else if(requestDto instanceof ReservationFuneralAgentDto){
            customData.put("translatedCertificateNo", ((ReservationFuneralAgentDto) requestDto).getTranslatedCertificateNo());
        }

        LoggingContext.sendCustomerEvent(response, topicName, topic, customData);

    }
}
