package com.pcp.funeralsvc.web.controller;

import com.pcp.funeralsvc.data.dto.request.*;
import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.data.dto.response.ResponseCode;
import com.pcp.funeralsvc.exception.LogicalException;
import com.pcp.funeralsvc.ipc.logAndEvent.EventSendUtil;
import com.pcp.funeralsvc.ipc.logAndEvent.message.LoggingContext;
import com.pcp.funeralsvc.service.FuneralService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pcp/funeral/v1/operator")
@CrossOrigin(origins = {"http://localhost:3000", "https://dashboard.d4fun.com"})
public class OperatorController {

    private FuneralService funeralService;

    @Autowired
    public OperatorController(FuneralService funeralService){
        this.funeralService = funeralService;
    }

    // 지역 등록 /pcp/funeral/v1/operator/funeral/region-create
    @RequestMapping(value = "/funeral/region-create", method = RequestMethod.POST)
    public Response regionCreateUpdate(@Valid @RequestBody RegionRequestDto regionRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "지역 등록");

        regionRequestDto.setUid(uid);
        Response response = funeralService.regionCreate(regionRequestDto);

        return (response);
    }

    // 지역 수정 /pcp/funeral/v1/operator/funeral/region-update
    @RequestMapping(value = "/funeral/region-update", method = RequestMethod.POST)
    public Response regionUpdate(@Valid @RequestBody RegionDetailRequestDto regionDetailRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "지역 수정");

        regionDetailRequestDto.setUid(uid);
        return (funeralService.regionUpdate(regionDetailRequestDto));
    }

    //전체 지역 조회 /pcp/funeral/v1/operator/funeral/region-search
    @GetMapping("/funeral/region-search")
    public Response getAllRegions(@RequestHeader("X-Uid") Long uid) throws Exception{

        if (!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "전체 지역 조회");
        return (funeralService.getAllRegions(uid));
    }

    //지역 삭제 /pcp/funeral/v1/operator/funeral/region-delete
    @RequestMapping(value = "/funeral/region-delete", method = RequestMethod.POST)
    public Response regionDelete(@RequestBody RegionIdRequestDto regionIdRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "지역 삭제");

        regionIdRequestDto.setUid(uid);
        return (funeralService.regionDelete(regionIdRequestDto));
    }

    //장묘지 정보 등록 /funeral/v1/operator/funeral/place-create
    @RequestMapping(value = "/funeral/place-create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response placeCreate(
            @RequestHeader("X-Uid") Long uid,
            @RequestPart("data") PlaceRequestDto placeRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {

        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "장묘지 정보 등록");

        placeRequestDto.setUid(uid);

        Response response = funeralService.placeCreate(placeRequestDto, file);

        EventSendUtil.sendCustomerEvent(uid, placeRequestDto, response, "장묘지 정보 등록", "operator.funeral.place-created");

        return response;
    }

    //장묘지 정보 수정 /pcp/funeral/v1/operator/funeral/place-detail-update
    @RequestMapping(value = "/funeral/place-detail-update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response operatorPlaceDetailUpdate(
            @RequestHeader("X-Uid") Long uid,
            @Valid @RequestPart("data") PlaceDetailRequestDto placeDetailRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {

        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "장묘지 정보 수정");

        placeDetailRequestDto.setUid(uid);
        return (funeralService.placeDetailUpdate(placeDetailRequestDto, file));
    }

    //장묘지 정보 삭제 /pcp/funeral/v1/operator/funeral/place-delete
    @RequestMapping(value = "/funeral/place-delete", method = RequestMethod.POST)
    public Response placeDelete(@Valid @RequestBody PlaceDetailRequestDto placeDetailRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {

        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "장묘지 정보 삭제");

        placeDetailRequestDto.setUid(uid);
        Response response = funeralService.placeDelete(placeDetailRequestDto);

        EventSendUtil.sendCustomerEvent(uid, placeDetailRequestDto, response, "장묘지 정보 삭제", "operator.funeral.place-deleted");

        return response;
    }

    //전체 장묘지 정보 검색 /pcp/funeral/v1/operator/funeral/place-search
    @GetMapping("/funeral/place-search")
    public Response placeAllSearch(@RequestHeader("X-Uid") Long uid) throws Exception{
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "전체 장묘지 정보 검색");

        return (funeralService.placeAllSearch(uid));
    }

    //장묘지 정보 상세 검색 /pcp/funeral/v1/operator/funeral/place-detail-search
    @GetMapping("/funeral/place-detail-search")
    public Response placeDetailSearch(@RequestHeader("X-Uid") Long uid, @RequestParam(value = "placeId", required = false) Long placeId) throws Exception{
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "장묘지 정보 상세 검색");

        return (funeralService.placeAllDetailSearch(uid, placeId));
    }

    //장묘 상담 예약 현황 검색 /pcp/funeral/v1/operator/funeral/reservation-list-search
    @GetMapping("/funeral/reservation-list-search")
    public Response reservationDetailSearch(@RequestHeader("X-Uid") Long uid) throws Exception{
        return null; //TODO 장묘 상담 예약 현황 검색
        //return (funeralService.placeDetailRetrieve(uid));
    }

    //공지 사항
    //1. 전체 운영자 공지 사항 검색 /pcp/funeral/v1/operator/board/search
    @GetMapping("/board/search")
    public Response boardAllSearch(@RequestHeader("X-Uid") Long uid) throws Exception{

        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 등록");

        return (funeralService.boardAllSearch(uid));
    }

    //2. 운영자 공지 사항 상세 검색 /pcp/funeral/v1/operator/board/detail-search
    @GetMapping("/board/detail-search")
    public Response boardDetailSearch(@RequestHeader("X-Uid") Long uid, @RequestParam(value = "boardId", required = false) Long boardId)  throws Exception{
        if(boardId == null){
            return new Response(ResponseCode.F02415);
        }
        return (funeralService.boardDetailSearch(uid, boardId));
    }

    //3. 운영자 공지 사항 등록 /pcp/funeral/v1/operator/board/create
    @RequestMapping(value = "/board/create", method = RequestMethod.POST)
    public Response boardCreate(@Valid @RequestBody BoardRequestDto boardRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 등록");

        boardRequestDto.setUid(uid);
        return (funeralService.boardCreate(boardRequestDto));
    }

    //4. 운영자 공지 사항 수정 /pcp/funeral/v1/operator/board/update
    @RequestMapping(value = "/board/update", method = RequestMethod.POST)
    public Response boardUpdate(@Valid @RequestBody BoardDetailRequestDto boardDetailRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 수정");

        return (funeralService.boardUpdate(boardDetailRequestDto));
    }

    //5. 운영자 공지 사항 삭제 /pcp/funeral/v1/operator/board/delete
    @RequestMapping(value = "/board/delete", method = RequestMethod.POST)
    public Response boardDelete(@RequestHeader("X-Uid") Long uid, @RequestParam("boardId") Long boardId) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 수정");

        return (funeralService.boardDelete(boardId));
    }

    //FAQ 기능
    //1. 운영자 FAQ 카테 고리 등록 /pcp/funeral/v1/operator/faq-category/create
    @RequestMapping(value = "/faq-category/create", method = RequestMethod.POST)
    public Response faqCategoryCreate(@Valid @RequestBody FaqCategoryRequestDto faqCategoryRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "FAQ 등록");

        faqCategoryRequestDto.setUid(uid);
        return (funeralService.faqCategoryCreate(faqCategoryRequestDto));
    }

    //2. 운영자 AQ 카테 고리 수정 /pcp/funeral/v1/operator/faq-category/update
    @RequestMapping(value = "/faq-category/update", method = RequestMethod.POST)
    public Response faqCategoryUpdate(@Valid @RequestBody FaqDetailCategoryRequestDto faqDetailCategoryRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 수정");

        return (funeralService.faqCategoryUpdate(faqDetailCategoryRequestDto));
    }

    //3. 운영자 FAQ 카테 고리 삭제 /pcp/funeral/v1/operator/faq-category/delete
    @RequestMapping(value = "/faq-category/delete", method = RequestMethod.POST)
    public Response faqCategoryDelete(@RequestHeader("X-Uid") Long uid, @RequestParam("categoryId") Long categoryId) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "공지 사항 수정");

        return (funeralService.faqCategoryDelete(categoryId));
    }

    //4. 운영자 FAQ 카테 고리 조회 /pcp/funeral/v1/operator/faq-category/search
    @GetMapping("/faq-category/search")
    public Response faqCategorySearch(@RequestHeader("X-Uid") Long uid) throws Exception{
        return (funeralService.faqCategorySearch(uid));
    }

    //5. 운영자 FAQ 등록 /pcp/funeral/v1/operator/faq/create
    @RequestMapping(value = "/faq/create", method = RequestMethod.POST)
    public Response faqCreate(@Valid @RequestBody FaqRequestDto faqRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "FAQ 등록");

        faqRequestDto.setUid(uid);
        return (funeralService.faqCreate(faqRequestDto));
    }

    //6. 운영자 FAQ 수정 /pcp/funeral/v1/operator/faq/update
    @RequestMapping(value = "/faq/update", method = RequestMethod.POST)
    public Response faqUpdate(@Valid @RequestBody FaqDetailRequestDto faqDetailRequestDto, @RequestHeader("X-Uid") Long uid) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "FAQ 수정");

        return (funeralService.faqUpdate(faqDetailRequestDto));
    }
    //7. 운영자 FAQ 삭제 /pcp/funeral/v1/operator/faq/delete
    @RequestMapping(value = "/faq/delete", method = RequestMethod.POST)
    public Response faqDelete(@RequestHeader("X-Uid") Long uid, @RequestParam("faqId") Long faqId) throws Exception {
        if(!funeralService.isUidValid(uid)) return funeralService.unKnownUid(uid, "FAQ 삭제");

        return (funeralService.faqDelete(faqId));
    }

    //8. 운영자 전체 FAQ 검색 /pcp/funeral/v1/operator/faq/search
    @GetMapping("/faq/search")
    public Response faqSearch(@RequestHeader("X-Uid") Long uid) throws Exception{
        return (funeralService.faqSearch(uid));
    }

    //9. 운영자 FAQ 상세 검색 /pcp/funeral/v1/operator/faq/detail-search
    @GetMapping("/faq/detail-search")
    public Response faqDetailSearch(@RequestHeader("X-Uid") Long uid, @RequestParam("faqId") Long faqId) throws Exception{
        return (funeralService.faqDetailSearch(uid, faqId));
    }

}
