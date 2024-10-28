package com.pcp.funeralsvc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pcp.funeralsvc.data.dto.responseFromAgent.ReservationFuneralAgentDto;
import com.pcp.funeralsvc.data.dto.request.*;
import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.data.entity.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface FuneralService {

    Response regionCreate(RegionRequestDto regionRequestDto) throws JsonProcessingException;
    Response regionUpdate(RegionDetailRequestDto regionDetailRequestDto) throws JsonProcessingException;
    Response regionDelete(RegionIdRequestDto regionIdRequestDto) throws JsonProcessingException;

    Response getAllRegions(Long uid) throws JsonProcessingException;

    Response placeCreate(PlaceRequestDto placeRequestDto, MultipartFile file) throws IOException;

    Response placeDetailUpdate(PlaceDetailRequestDto placeRequestDto, MultipartFile file) throws IOException;

    Response placeAllSearch(Long uid) throws JsonProcessingException;

    Response placeAllDetailSearch(Long uid, Long placeId) throws JsonProcessingException;

    Response placeDelete(PlaceDetailRequestDto placeRequestDto) throws JsonProcessingException;

    Response outboundRequest(OutboundRequestDto outboundRequestDto) throws Exception;

    Response favoritePlaceSetRequest(FavoritePlaceSetRequestDto favoritePlaceSetRequestDto) throws Exception;

    Response favoritePlaceSearch(Long uid) throws Exception;

    Response reservationConfirm(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception;

    Response reservationUpdate(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception;

    Response reservationCancel(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception;

    Response reservationFuneralComplete(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception;

    ResponseEntity<?> imageDownload(Long uid, Long place_id) throws JsonProcessingException;

    boolean isUidValid(Long uid);
    Response unKnownUid(Long uid, String prefix) throws Exception;
    Response userInfoChange(UserInfoChangeDto userInfoChangeDto) throws Exception;

    //공지 사항/ FAQ

    Response boardCreate(BoardRequestDto boardRequestDto) throws Exception;

    Response boardAllSearch(Long uid) throws Exception;

    Response boardDetailSearch(Long uid, Long noticeId) throws Exception;

    Response boardUpdate(BoardDetailRequestDto boardDetailRequestDto) throws Exception;

    Response boardDelete(Long boardId) throws Exception;

    Response faqCategoryCreate(FaqCategoryRequestDto faqCategoryRequestDto) throws Exception;

    Response faqCategoryUpdate(FaqDetailCategoryRequestDto faqDetailCategoryRequestDto) throws Exception;

    Response faqCategoryDelete(Long categoryId) throws Exception;

    Response faqCategorySearch(Long uid) throws Exception;

    Response faqCreate(FaqRequestDto faqRequestDto) throws Exception;

    Response faqUpdate(FaqDetailRequestDto faqDetailRequestDto) throws Exception;
    Response faqDelete(Long faqId) throws Exception;

    Response faqSearch(Long uid) throws Exception;

    Response faqDetailSearch(Long uid, Long faqId) throws Exception;


}
