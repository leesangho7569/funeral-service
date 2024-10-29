package com.pcp.funeralsvc.web.controller;

import com.pcp.funeralsvc.data.dto.request.FavoritePlaceSetRequestDto;
import com.pcp.funeralsvc.data.dto.request.OutboundRequestDto;
import com.pcp.funeralsvc.data.dto.request.PlaceDetailRequestDto;
import com.pcp.funeralsvc.data.dto.request.UserInfoChangeDto;
import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.ipc.logAndEvent.EventSendUtil;
import com.pcp.funeralsvc.ipc.logAndEvent.message.LoggingContext;
import com.pcp.funeralsvc.service.FuneralService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pcp/funeral/v1/customer")
public class CustomerController {

    private FuneralService funeralService;

    @Autowired
    public CustomerController(FuneralService funeralService){
        this.funeralService = funeralService;
    }

    //전체 지역 조회 /pcp/funeral/v1/customer/funeral/region-search
    @GetMapping("/funeral/region-search")
    public Response getAllRegions(@RequestHeader("X-Uid") Long uid) throws Exception{

        if (!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "전체 지역 조회");
        return (funeralService.getAllRegions(uid));
    }

    //전체 장묘지 검색 /pcp/funeral/v1/customer/funeral/place-search
    @GetMapping("/funeral/place-search")
    public Response getPlaces(@RequestHeader("X-Uid") Long uid) throws Exception{
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "전체 장묘지 검색");
        return (funeralService.placeAllSearch(uid));
    }

    //장묘지 상세 검색 /pcp/funeral/v1/customer/funeral/place-detail-search
    @GetMapping("/funeral/place-detail-search")
    public Response placeDetailSearch(@RequestHeader("X-Uid") Long uid, @RequestParam(value = "placeId", required = false) Long placeId) throws Exception{
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "장묘지 상세 검색");
        return (funeralService.placeAllDetailSearch(uid, placeId));
    }

    //장묘 상담 요청(Outbound) /pcp/funeral/v1/customer/funeral/outbound-request
    @RequestMapping(value = "/funeral/outbound-request", method = RequestMethod.POST)
    public Response outboundRequest(@Valid @RequestBody OutboundRequestDto outboundRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception{

        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "장묘 상담 요청(Outbound)");
        outboundRequestDto.setUid(uid);
        Response response = funeralService.outboundRequest(outboundRequestDto);
//        LoggingContext.customer().id(Long.toString(uid));
//        Map<String, Object> customData = null;
//        if(outboundRequestDto.getTranslatedCertificateNo() != null) {
//            customData = new HashMap<>();
//            customData.put("translatedCertificateNo", outboundRequestDto.getTranslatedCertificateNo());
//        }
//
//        LoggingContext.sendCustomerEvent(response, "장묘 상담 요청", "customer.funeral.outbound-requested", customData);

        EventSendUtil.sendCustomerEvent(uid, outboundRequestDto, response, "장묘 상담 요청(Outbound)", "customer.funeral.outbound-requested");

        return response;
    }


    //선호 장묘지 설정 /pcp/funeral/v1/customer/funeral/favorite-place-set
    @RequestMapping(value = "/funeral/favorite-place-set", method = RequestMethod.POST)
    public Response favoritePlaceSet(@Valid @RequestBody FavoritePlaceSetRequestDto favoritePlaceSetRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception{
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "선호 장묘지 설정");
        favoritePlaceSetRequestDto.setUid(uid);
        return (funeralService.favoritePlaceSetRequest(favoritePlaceSetRequestDto));
    }

    //선호 장묘지 검색 /pcp/funeral/v1/customer/funeral/favorite-place-search
    @GetMapping("funeral/favorite-place-search")
    public Response favoritePlaceSearch(@RequestHeader("X-Uid") Long uid) throws Exception{
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "선호 장묘지 장묘지 검색");
        return (funeralService.favoritePlaceSearch(uid));
    }

    //고객 번호 변경 및 주소 변경 전달 API /pcp/funeral/v1/customer/internal/user-info-change
    @RequestMapping(value = "/internal/user-info-change", method = RequestMethod.POST)
    public Response userInfoChange(@RequestBody UserInfoChangeDto userInfoChangeDto) throws Exception{
        return (funeralService.userInfoChange(userInfoChangeDto));
    }


    //공지 사항
    //1. 전체 운영자 공지 사항 검색 /pcp/funeral/v1/customer/board/search
    @GetMapping("/board/search")
    public Response boardAllSearch(@RequestHeader("X-Uid") Long uid) throws Exception{

        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 등록");

        return (funeralService.boardAllSearch(uid));
    }

    //2. 고객 공지 사항 상세 검색 /pcp/funeral/v1/customer/board/detail-search
    @GetMapping("/board/detail-search")
    public Response boardDetailSearch(@RequestHeader("X-Uid") Long uid, @RequestParam("boardId") Long boardId) throws Exception{
        return (funeralService.boardDetailSearch(uid, boardId));
    }

    //8. 고객 전체 FAQ 검색 /pcp/funeral/v1/customer/faq/search
    @GetMapping("/faq/search")
    public Response faqSearch(@RequestHeader("X-Uid") Long uid) throws Exception{
        return (funeralService.faqSearch(uid));
    }

    //9. 고객 FAQ 상세 검색 /pcp/funeral/v1/customer/faq/detail-search
    @GetMapping("/faq/detail-search")
    public Response faqDetailSearch(@RequestHeader("X-Uid") Long uid, @RequestParam("faqId") Long faqId) throws Exception{
        return (funeralService.faqDetailSearch(uid, faqId));
    }

    @GetMapping("/image-download/{place_id}")
    public ResponseEntity<?> funeralPlaceImageDownLoad(@RequestHeader("X-Uid") Long uid, @PathVariable Long place_id) throws Exception{
        return (funeralService.imageDownload(0L, place_id));
    }


}
