package com.pcp.funeralsvc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcp.funeralsvc.data.dto.FuneralAgentRequestEnum;
import com.pcp.funeralsvc.data.dto.response.*;
import com.pcp.funeralsvc.data.dto.responseFromAgent.ReservationFuneralAgentDto;
import com.pcp.funeralsvc.data.dto.request.*;
import com.pcp.funeralsvc.data.dto.requestToAgent.ReservationRequestDto;

import com.pcp.funeralsvc.data.entity.*;
import com.pcp.funeralsvc.data.repository.*;
import com.pcp.funeralsvc.exception.LogicalException;
import com.pcp.funeralsvc.ipc.logAndEvent.dto.CustomerEventDto;
import com.pcp.funeralsvc.ipc.logAndEvent.message.event.CustomerEventBuilder;
import com.pcp.funeralsvc.ipc.producer.Producer;
import com.pcp.funeralsvc.ipc.logAndEvent.message.LoggingContext;

import com.pcp.funeralsvc.ipc.logAndEvent.message.event.OperatorEventBuilder;
import com.pcp.funeralsvc.ipc.logAndEvent.message.log.GeneralLog;
import com.pcp.funeralsvc.service.FuneralService;
import com.pcp.funeralsvc.utils.Json;
import com.pcp.funeralsvc.web.external.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;

import static com.pcp.funeralsvc.data.dto.FuneralAgentRequestEnum.SERVICE_RESERVATION_CONFIRM;

@Service
public class FuneralServiceImpl implements FuneralService {

    private final Logger logger = LoggerFactory.getLogger(FuneralServiceImpl.class);

    private Producer producer;
    private RegionRepository regionRepository;
    private PlaceRepository placeRepository;
    private ReservationRepository reservationRepository;
    private FavoritePlaceRepository favoritePlaceRepository;
    private UsersInfoRepository usersInfoRepository;
    private ReservationResRepository reservationResRepository;
    private BoardRepository boardRepository;
    private FaqsCategoryRepository faqsCategoryRepository;
    private FaqsRepository faqsRepository;
    private ReservationResHistoryRepository reservationResHistoryRepository;
    private ReservationHistoryRepository reservationHistoryRepository;

    private HttpClient httpClient;

    @Autowired
    public FuneralServiceImpl(Producer producer, RegionRepository regionRepository, PlaceRepository placeRepository,
                              ReservationRepository reservationRepository, FavoritePlaceRepository favoritePlaceRepository,
                              UsersInfoRepository usersInfoRepository, ReservationResRepository reservationResRepository,
                              BoardRepository boardRepository, FaqsCategoryRepository faqsCategoryRepository,
                              FaqsRepository faqsRepository, ReservationResHistoryRepository reservationResHistoryRepository,
                              ReservationHistoryRepository reservationHistoryRepository, HttpClient httpClient){

        this.producer = producer;
        this.regionRepository = regionRepository;
        this.placeRepository = placeRepository;
        this.reservationRepository = reservationRepository;
        this.favoritePlaceRepository = favoritePlaceRepository;
        this.usersInfoRepository = usersInfoRepository;
        this.reservationResRepository = reservationResRepository;
        this.boardRepository = boardRepository;
        this.faqsCategoryRepository = faqsCategoryRepository;
        this.faqsRepository = faqsRepository;
        this.reservationResHistoryRepository = reservationResHistoryRepository;
        this.reservationHistoryRepository = reservationHistoryRepository;
        this.httpClient = httpClient;
    }

    @Override
    public Response regionUpdate(RegionDetailRequestDto regionDetailRequestDto) throws JsonProcessingException {
        String msg, key, prefix;
        prefix = "지역 수정";
        key = Long.toString(regionDetailRequestDto.getUid());
        msg = generateMessage(regionDetailRequestDto, prefix);

        Region region = regionRepository.findById(regionDetailRequestDto.getRegionId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02409));

        region.setRegionName(regionDetailRequestDto.getRegionName());
        regionRepository.save(region);

        logger.info("{} | {} | {} 완료", prefix, key, msg);

        return new Response(ResponseCode.S00000, new RegionResponseDto(region));
    }

    @Override
    public Response regionCreate(RegionRequestDto regionRequestDto) throws JsonProcessingException {

        String msg, key, prefix;
        prefix = "지역 생성";

        key = Long.toString(regionRequestDto.getUid());
        msg = generateMessage(regionRequestDto, prefix);

        Optional<Region> regionOptional = regionRepository.findByRegionName(regionRequestDto.getRegionName());
        
        if(regionOptional.isPresent()){
            msg = msg + " already exists.";
            logger.error("{} | {} | {} ", prefix, regionRequestDto.getUid(), msg);
            return response(ResponseCode.F02001, prefix + " 에러", key, LoggingContext.general().error(key,msg), null);
        }

        Region region = new Region();
        region.setRegionName(regionRequestDto.getRegionName());

        regionRepository.save(region);

        logger.info("{} | {} | {} 완료", prefix, regionRequestDto.getUid(), msg);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response regionDelete(RegionIdRequestDto regionIdRequestDto) throws JsonProcessingException {

        String msg, key, prefix;
        prefix = "지역 삭제";
        key = String.format(" %d | %d", regionIdRequestDto.getUid());
        msg = String.format(" %s | 장묘지 = %d", prefix, regionIdRequestDto.getRegionId());
        regionRepository.deleteById(regionIdRequestDto.getRegionId());

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response getAllRegions(Long uid) throws JsonProcessingException{
        String msg, key, prefix;
        prefix = "전체 지역 검색";
        key = String.format("%d", uid);
        msg = prefix;
        logger.info("{} | {} | {}", prefix, key, msg);

//        List<Region> regionList = new ArrayList<>();
//        regionRepository.findAll().forEach(regionList::add);

        List<RegionResponseDto> responseData = regionRepository.findAll().stream()
                .map(RegionResponseDto::new)
                .toList();

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), responseData);
    }

    @Override
    public Response placeCreate(PlaceRequestDto placeRequestDto, MultipartFile file) throws IOException {

        String msg, key, prefix;
        prefix = "장묘지 정보 등록";
        key = String.format("%d", placeRequestDto.getUid());
        msg = String.format("%s | 장묘 지 이름 = %s.", prefix, placeRequestDto.getPlaceName());

        Region region = regionRepository.findById(placeRequestDto.getRegionId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02411));

        Optional<Place> placeOptional = placeRepository.findByPlaceName(placeRequestDto.getPlaceName());
        if(placeOptional.isPresent()){
            msg = msg + "already exists.";
            logger.error("{} ", msg);
            return response(ResponseCode.F02002, prefix + " 에러", key, LoggingContext.general().error(key,msg), null);
        }

        byte[] imageBytes = file.getBytes();

        Place place = Place.builder()
                .placeName(placeRequestDto.getPlaceName())
                .introduction(placeRequestDto.getIntroduction())
                .address(placeRequestDto.getAddress())
                .region(region)
                .imageFile(file.getOriginalFilename())
                .imageData(imageBytes)
                .build();

        placeRepository.save(place);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);

    }

    @Override
    public Response placeDetailUpdate(PlaceDetailRequestDto placeDetailRequestDto, MultipartFile file) throws IOException {

        OperatorEventBuilder operatorEventBuilder = LoggingContext.operator();
        operatorEventBuilder.name("-");
        operatorEventBuilder.division("-");
        operatorEventBuilder.type("-");
        operatorEventBuilder.customData(convertPlaceToMap(placeDetailRequestDto, file != null ? file.getOriginalFilename():null));

        String msg, key, prefix;
        prefix = "장묘지 정보 수정";
        key = Long.toString(placeDetailRequestDto.getUid());
        msg = generateMessage(placeDetailRequestDto, prefix);
        if(file != null && !file.isEmpty()){
            msg = msg + file.getOriginalFilename();
        }

        Place place = findPlaceById(placeDetailRequestDto.getPlaceId(), prefix);

        Region region = null;
        if(placeDetailRequestDto.getRegionId() != null) {
            region = findRegionById(placeDetailRequestDto.getRegionId(), prefix);
        }

        place.updatePlace(placeDetailRequestDto, region, file);
        placeRepository.save(place);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response placeAllSearch(Long uid) throws JsonProcessingException {
        //List<PlaceDetailResponseDto> placeResponseDtos = placeRepository.findAllPlacesWithRegion();

        List<PlaceWithFavoriteResponseDto> placeResponseDtos = placeRepository.findAllPlacesWithFavoriteByUid(uid);

        logger.info("전체 장묘지 정보 검색 | {} | ", uid);

        return new Response(ResponseCode.S00000, placeResponseDtos);
    }

    @Override
    public Response placeAllDetailSearch(Long uid, Long placeId) throws JsonProcessingException {

//        List<PlaceDetailResponseDto> placeResponseDtos = placeRepository.findAllPlacesWithRegion();
//
//        return new Response(ResponseCode.S00000, placeResponseDtos);

        Optional<Place> placeOptional = placeRepository.findById(placeId);
        if(placeOptional.isEmpty()){
            logger.error("장묘지 정보 검색 | 존재 하지 않는 정보 = {}", placeId);
            return new Response(ResponseCode.F02411);
        }

        return new Response(ResponseCode.S00000, placeOptional.get());
    }

    @Override
    public Response placeDelete(PlaceDetailRequestDto placeDetailRequestDto) throws JsonProcessingException {

        String msg, key, prefix;
        prefix = "장묘지 정보 삭제";
        key = String.format("%d", placeDetailRequestDto.getUid());
        msg = String.format(" %s | 장묘 지역 정보 = %d", prefix, placeDetailRequestDto.getPlaceId());
        placeRepository.deleteById(placeDetailRequestDto.getPlaceId());

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response outboundRequest(OutboundRequestDto outboundRequestDto) throws Exception {

        String msg, key, prefix;
        Long uid = outboundRequestDto.getUid();
        prefix = "장묘 예약 상담 요청";

        key = Long.toString(uid);

        //Optional<ServiceInfo> serviceInfoOptional = serviceInfoRepository.findById(outboundRequestDto.getTranslatedCertificateNo());
        List<UserInfoServiceDto> userInfoServiceDtoList = reservationResRepository.findActionsByUid(uid, outboundRequestDto.getTranslatedCertificateNo());

        if(userInfoServiceDtoList == null || userInfoServiceDtoList.isEmpty()){

            Response response;
            Optional<Response> dto = httpClient.sendUserInfo(new UserServiceRequestDto(outboundRequestDto.getTranslatedCertificateNo()));
            response = dto.get();
            if(response.getResponseCode() != ResponseCode.S00000) {
                msg = String.format(" invalid user-service-응답 = %s", response.getResponseCode());
                logger.error("{} | {} ", prefix, msg);
                return response(ResponseCode.F02007, prefix, key, LoggingContext.general().error(key, msg), null);
            }
            FromUserServiceDto fromUserServiceDto = Json.toObjectConvert(response.getResponseData(), FromUserServiceDto.class);
            logger.info("{} | {}", prefix, fromUserServiceDto);

            UsersInfo usersInfo = null;
            Optional<UsersInfo> usersInfoOptional = usersInfoRepository.findById(uid);
            if(usersInfoOptional.isPresent()){
                usersInfo = usersInfoOptional.get();
            }
            else{
                usersInfo = new UsersInfo();
                usersInfo.setUid(uid);
            }
            usersInfo.setMobile(fromUserServiceDto.getMobile());
            usersInfo.setCustomerName(fromUserServiceDto.getCustomerName());
            usersInfo.setPassAppId(fromUserServiceDto.getPassAppId());
            usersInfoRepository.save(usersInfo);

            ReservationRes reservationRes = null;
            Optional<ReservationRes> reservationResOptional = reservationResRepository.findById(outboundRequestDto.getTranslatedCertificateNo());
            if(reservationResOptional.isPresent()){
                reservationRes = reservationResOptional.get();
            }
            else {
                reservationRes = new ReservationRes();
                reservationRes.setUid(uid);
            }

            reservationRes.setFuneralCertificateNo(fromUserServiceDto.getFuneralCertificateNo());
            reservationRes.setTranslatedCertificateNo(outboundRequestDto.getTranslatedCertificateNo());
            reservationResRepository.save(reservationRes);

            outboundRequestDto.setFuneralCertificateNo(reservationRes.getFuneralCertificateNo());
            outboundRequestDto.setTranslatedCertificateNo(reservationRes.getTranslatedCertificateNo());
            outboundRequestDto.setMobile(usersInfo.getMobile());
            outboundRequestDto.setCustomerName(usersInfo.getCustomerName());
        }
        else{
            outboundRequestDto.setFuneralCertificateNo(userInfoServiceDtoList.get(0).getFuneralCertificateNo());
            outboundRequestDto.setTranslatedCertificateNo(userInfoServiceDtoList.get(0).getTranslatedCertificateNo());
            outboundRequestDto.setMobile(userInfoServiceDtoList.get(0).getMobile());
            outboundRequestDto.setCustomerName(userInfoServiceDtoList.get(0).getCustomerName());
        }

        msg = generateMessage(outboundRequestDto, prefix);
        logger.info("{} | {} | {}", prefix, key, msg);

        Reservation reservation = null;

        //희망 장묘지는 선호 장묘지의 리스트를 사용한다.

        List<String> placeNames = favoritePlaceRepository.findFavoritePlaceByUid(outboundRequestDto.getUid());

        long startTime = System.currentTimeMillis();
        //ResponseFromAgentDto reservationResponseDto = null;
        Response response = null;

        try {
            ReservationRequestDto requestDto = makeReservationRequestDto(outboundRequestDto, placeNames, outboundRequestDto.getVehicleSvcSupport());
            Optional<Response> dto = httpClient.sendReservationRequest(requestDto);
            response = dto.get();

            if(response.getResponseCode() != ResponseCode.S00000){
                logger.error("장묘 예약 응답 : 실패 = {}/{}", response.getResponseCode(), response.getResponseMessage());
                msg = msg + " 실패 = " + response.getResponseMessage();
                return response(response.getResponseCode(), prefix, key, LoggingContext.general().error(key,msg), null);
            }

            Optional<Reservation> reservationOptional = reservationRepository.findById(outboundRequestDto.getTranslatedCertificateNo());
            reservation = makeReservation(reservationOptional, outboundRequestDto, placeNames);

            reservationRepository.save(reservation);
            saveReservationHistory(reservation, placeNames, reservationOptional.isPresent()?"update":"create");

        }catch (ResourceAccessException e){
            logger.error("장묘 예약 응답 : Connection refused: Unable to connect to the server. Please check if the server is running");
            e.printStackTrace();
            msg = msg + "Connection refused: 상조사 서버를 연결 할 수 없 습니다. 서버를 확인 해 주세요.";
            return response(ResponseCode.F02500, prefix, key, LoggingContext.general().error(key,msg), null);
        }
        catch (HttpClientErrorException e){

            // 404 에러 처리
            if (e.getStatusCode().value() == 404) {
                logger.error("장묘 예약 응답 : 상조사 시스템 므응답 404 Not Found Error: {}", e.getResponseBodyAsString());
                return response(ResponseCode.F02404, prefix, key, LoggingContext.general().info(key,msg), null);
            } else {
                // 다른 HTTP 에러 처리
                logger.error("장묘 예약 응답 : 상조사 시스템 므응답 HttpClientErrorException: {}", e.getResponseBodyAsString());
                return response(ResponseCode.F02500, prefix, key, LoggingContext.general().info(key,msg), null);
            }

        }

        logger.info("장묘 예약 응답 : Elapse time = {}", System.currentTimeMillis()-startTime);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response favoritePlaceSetRequest(FavoritePlaceSetRequestDto favoritePlaceSetRequestDto) throws Exception {

        String msg, key, prefix;
        prefix = "선호 장묘지 설정 요청";
        key = Long.toString(favoritePlaceSetRequestDto.getUid());
        msg = generateMessage(favoritePlaceSetRequestDto, prefix);
        logger.info("{} | {} | {}", prefix, key, msg);
        Place place = placeRepository.findById(favoritePlaceSetRequestDto.getPlaceId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02411));

        FavoritePlace favoritePlace = new FavoritePlace(
                new FavoritePlacePkId(favoritePlaceSetRequestDto.getUid(), favoritePlaceSetRequestDto.getPlaceId()),
                place, favoritePlaceSetRequestDto.getFavorite());
        favoritePlaceRepository.save(favoritePlace);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response favoritePlaceSearch(Long uid) throws Exception {

        List<FavoritePlaceResponseDto> favoritePlaceResponseDtoList = favoritePlaceRepository.findAllFavoritePlacesByUid(uid);

        if(favoritePlaceResponseDtoList == null || favoritePlaceResponseDtoList.size() == 0){
            return new Response(ResponseCode.F02410);
        }
        return new Response(ResponseCode.S00000, favoritePlaceResponseDtoList);
    }

    @Override
    public Response reservationConfirm(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception {

        String prefix = "장묘 서비스 예약 확정";

        return handleAction(reservationFuneralAgentDto, prefix, SERVICE_RESERVATION_CONFIRM);
    }

    @Override
    public Response reservationUpdate(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception {
        String prefix = "장묘 서비스 예약 수정";
        logger.info("{}", prefix);

        return handleAction(reservationFuneralAgentDto, prefix, FuneralAgentRequestEnum.SERVICE_RESERVATION_UPDATE);
    }

    @Override
    public Response reservationCancel(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception {
        String prefix = "장묘 서비스 예약 취소";
        logger.info("{}", prefix);

        return handleAction(reservationFuneralAgentDto, prefix, FuneralAgentRequestEnum.SERVICE_RESERVATION_CANCEL);
    }

    @Override
    public Response reservationFuneralComplete(ReservationFuneralAgentDto reservationFuneralAgentDto) throws Exception {

        String prefix = "장묘 서비스 이용 완료";
        logger.info("{}", prefix);

        // TODO customer-event 정의
//        CustomerEventBuilder customerEventBuilder = LoggingContext.customer();
//        customerEventBuilder.name("-");
//        customerEventBuilder.division("-");
//        customerEventBuilder.type("-");
        //customerEventBuilder.customData(convertPlaceToMap(placeDetailRequestDto, file != null ? file.getOriginalFilename():null));

        return handleAction(reservationFuneralAgentDto, prefix, FuneralAgentRequestEnum.SERVICE_USE_COMPLETE);
    }

    @Override
    public ResponseEntity<?> imageDownload(Long uid, Long place_id) throws JsonProcessingException {

        Place place = findPlaceById(place_id, "이미지 다운 로드");

        byte[] data = place.getImageData();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                //.header("Content-type", "application/octet-stream")
                .header("Content-type", "image/png")
                .header("Content-disposition", "attachment; filename=\"" + place.getImageFile() + "\"")
                .body(resource);

    }

    @Override
    public boolean isUidValid(Long uid) {
//        return usersInfoRepository.existsById(uid);
        return true;
    }

    @Override
    public Response unKnownUid(Long uid, String prefix) throws Exception {

        String msg = String.format("%s | 알수 없는 uid = %d", prefix, uid);
        logger.error("{}", msg);
        return response(ResponseCode.F02404, prefix, uid.toString(), LoggingContext.general().error(uid.toString(),msg), null);
    }

    @Override
    public Response userInfoChange(UserInfoChangeDto userInfoChangeDto) throws Exception {

        String prefix = "고객 번호 변경 및 주소 변경 전달";
        String key = userInfoChangeDto.getFuneralCertificateNo();
        String msg = prefix;
        logger.info("{} | {}", prefix, userInfoChangeDto);

        Optional<Response> dto = httpClient.sendFuAgentRequest(userInfoChangeDto);
        Response response = dto.get();
        if(response.getResponseCode() != ResponseCode.S00000){
            logger.error("장묘 예약 응답 : 실패 = {}", response.getResponseMessage());
            msg = msg + " 실패 = " + response.getResponseMessage();
            return response(ResponseCode.F02500, prefix, key, LoggingContext.general().error(key,msg), null);
        }

        return new Response(ResponseCode.S00000);
    }

    @Override
    public Response boardCreate(BoardRequestDto boardRequestDto) throws Exception {

        String msg, key, prefix;
        prefix = "공지 사항 등록";
        key = Long.toString(boardRequestDto.getUid());
        msg = generateMessage(boardRequestDto, prefix);

        Board board = new Board();
//        Optional<Board> noticeOptional = boardRepository.findByTitle(boardRequestDto.getTitle());
//
//        if(noticeOptional.isPresent()){
//            board = noticeOptional.get();
//        }
//        else{
//            board = new Board();
//        }
        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());

        boardRepository.save(board);

        logger.info("{} | {} | {} 완료", prefix, key, msg);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response boardAllSearch(Long uid) throws Exception {

        logger.debug("{} | 공지 사항 전체 검색", uid);

        List<BoardResponseDto> responseData = boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .toList();

        return new Response(ResponseCode.S00000, responseData);
    }

    @Override
    public Response boardDetailSearch(Long uid, Long noticeId) throws Exception {

        logger.debug("{} | {} | 공지 사항 상체 검색", uid, noticeId);

        List<BoardDetailResponseDto> responseData = boardRepository.findAll().stream()
                .map(BoardDetailResponseDto::new)
                .toList();

        return new Response(ResponseCode.S00000, responseData);
    }

    @Override
    public Response boardUpdate(BoardDetailRequestDto boardDetailRequestDto) throws Exception {

        Board responseData = boardRepository.findByBoardId(boardDetailRequestDto.getBoardId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02414));

        responseData.setTitle(boardDetailRequestDto.getTitle());
        responseData.setContent(boardDetailRequestDto.getContent());

        boardRepository.save(responseData);

        return new Response(ResponseCode.S00000, responseData);
    }

    @Override
    public Response boardDelete(Long noticeId) throws Exception {
        boardRepository.deleteById(noticeId);
        return new Response(ResponseCode.S00000);
    }

    @Override
    public Response faqCategoryCreate(FaqCategoryRequestDto faqCategoryRequestDto) throws Exception {

        String msg, key, prefix;
        prefix = "FAQ 카테 고리 등록";
        key = Long.toString(faqCategoryRequestDto.getUid());
        msg = generateMessage(faqCategoryRequestDto, prefix);

        FaqCategory faqsCategory = new FaqCategory();
        faqsCategory.setCategoryName(faqCategoryRequestDto.getCategoryName());

        faqsCategoryRepository.save(faqsCategory);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response faqCategoryUpdate(FaqDetailCategoryRequestDto faqDetailCategoryRequestDto) throws Exception {

        FaqCategory responseData = faqsCategoryRepository.findById(faqDetailCategoryRequestDto.getCategoryId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02414));

        responseData.setCategoryName(faqDetailCategoryRequestDto.getCategoryName());

        faqsCategoryRepository.save(responseData);

        return new Response(ResponseCode.S00000, responseData);
    }

    @Override
    public Response faqCategoryDelete(Long categoryId) throws Exception {
        faqsCategoryRepository.deleteById(categoryId);
        return new Response(ResponseCode.S00000);
    }

    @Override
    public Response faqCategorySearch(Long uid) throws Exception {

        logger.debug("{} | 전체 FAQ 카테 고리 검색", uid);

        List<FaqCategoryResponseDto> responseData = faqsCategoryRepository.findAll().stream()
                .map(FaqCategoryResponseDto::new)
                .toList();

        return new Response(ResponseCode.S00000, responseData);

    }

    @Override
    public Response faqCreate(FaqRequestDto faqRequestDto) throws Exception {

        String msg, key, prefix;
        prefix = "FAQ 등록";
        key = Long.toString(faqRequestDto.getUid());
        msg = generateMessage(faqRequestDto, prefix);

        FaqCategory category = faqsCategoryRepository.findById(faqRequestDto.getCategoryId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02414));

        Faq faqs = new Faq();
        faqs.setCategory(category);
        faqs.setQuestion(faqRequestDto.getQuestion());
        faqs.setAnswer(faqRequestDto.getAnswer());

        faqsRepository.save(faqs);

        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    @Override
    public Response faqUpdate(FaqDetailRequestDto faqDetailRequestDto) throws Exception {

        Faq responseData = faqsRepository.findById(faqDetailRequestDto.getFaqId())
                .orElseThrow(() -> new LogicalException(ResponseCode.F02416));

        responseData.setQuestion(faqDetailRequestDto.getQuestion());
        responseData.setAnswer(faqDetailRequestDto.getAnswer());

        faqsRepository.save(responseData);

        return new Response(ResponseCode.S00000, responseData);
    }

    @Override
    public Response faqDelete(Long faqId) throws Exception {

        faqsRepository.deleteById(faqId);
        return new Response(ResponseCode.S00000);
    }

    @Override
    public Response faqSearch(Long uid) throws Exception {

        logger.debug("{} | 전체 FAQ 검색", uid);

        List<FaqResponseDto> responseData = faqsRepository.findAll().stream()
                .map(FaqResponseDto::new)
                .toList();
        return new Response(ResponseCode.S00000, responseData);
    }

    @Override
    public Response faqDetailSearch(Long uid, Long faqId) throws Exception {

        logger.debug("{} | 전체 FAQ 상세 검색", uid);

        Faq faq = faqsRepository.findById(faqId)
                .orElseThrow(() -> new LogicalException(ResponseCode.F02416));

        return new Response(ResponseCode.S00000, new FaqResponseDto(faq));

    }

    private Response handleAction(ReservationFuneralAgentDto reservationFuneralAgentDto, String prefix, FuneralAgentRequestEnum funeralAgentRequestEnum) throws JsonProcessingException{

        logger.info("{} | PCP={} | 상담 예약 장묘지={} /장묘 시간={}", prefix, reservationFuneralAgentDto.getTranslatedCertificateNo(),
                reservationFuneralAgentDto.getFuneralPlace(), reservationFuneralAgentDto.getFuneralDate());

        Reservation reservation = findReservation(reservationFuneralAgentDto.getTranslatedCertificateNo());
        reservationFuneralAgentDto.setUid(reservation.getUid());

        handleServiceAction(reservationFuneralAgentDto, reservation, funeralAgentRequestEnum);

        String msg, key;
        key = Long.toString(reservation.getUid());
        msg = generateMessage(reservationFuneralAgentDto, prefix);
        //logger.info("{} | {}", prefix, msg);
        return response(ResponseCode.S00000, prefix, key, LoggingContext.general().info(key,msg), null);
    }

    private void handleServiceAction(ReservationFuneralAgentDto reservationFuneralAgentDto, Reservation reservation, FuneralAgentRequestEnum funeralAgentRequestEnum){

        ReservationRes reservationRes = null;
        Optional<ReservationRes> reservationResOptional = reservationResRepository.findById(reservation.getTranslatedCertificateNo());
        if(reservationResOptional.isPresent()){
            reservationRes = reservationResOptional.get();
        }
        else{
            reservationRes = new ReservationRes();
            reservationRes.setUid(reservation.getUid());
            reservationRes.setTranslatedCertificateNo(reservationFuneralAgentDto.getTranslatedCertificateNo());
        }

        if(funeralAgentRequestEnum == SERVICE_RESERVATION_CONFIRM){
            reservationRes.setFuneralDate(reservationFuneralAgentDto.getFuneralDate());
            reservationRes.setFuneralPlace(reservationFuneralAgentDto.getFuneralPlace());
        }

        logger.info("{} | {}", funeralAgentRequestEnum.getMsg(), reservationFuneralAgentDto);

        reservationRes.setReservationType(funeralAgentRequestEnum.getCode());
        reservationRes.setReservationState(funeralAgentRequestEnum.getMsg());

        reservationResRepository.save(reservationRes);

        saveReservationResHistory(reservationRes);
    }

    private Response response(ResponseCode responseCode, String prefix, String key, GeneralLog generalLog, Object object) {

        producer.publish(LoggingContext.general_log_routing_key, generalLog);
        if(object != null){
            return new Response(responseCode, object);
        }

        return new Response(responseCode);
    }

    private Reservation makeReservation(Optional<Reservation> reservationOptional, OutboundRequestDto outboundRequestDto, List<String> placeNames) throws Exception {

        Reservation reservation = null;
        if(reservationOptional.isPresent()){
            reservation = reservationOptional.get();
        }
        else {
            reservation = new Reservation();
            reservation.setTranslatedCertificateNo(outboundRequestDto.getTranslatedCertificateNo());
        }

        if(placeNames != null && placeNames.size() > 0){
            String favoritePlaceNames = Json.toStringJson(placeNames);
            logger.info("favoritePlaceNames = {}", favoritePlaceNames);
            reservation.setFavoritePlaceNames(favoritePlaceNames);
            //reservation.setCemeteryName(placeNames);
        }

        reservation.setUid(outboundRequestDto.getUid());
        reservation.setFuneralCertificateNo(outboundRequestDto.getFuneralCertificateNo());
        reservation.setVehicleSvcSupport(outboundRequestDto.getVehicleSvcSupport());

        return reservation;
    }
    private ReservationRequestDto makeReservationRequestDto(OutboundRequestDto outboundRequestDto, List<String> placeNames, Boolean vehicleSvcSupport ){

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        if(placeNames != null && placeNames.size() > 0){
            reservationRequestDto.setCemeteryName(placeNames);
        }

        reservationRequestDto.setTranslatedCertificateNo(outboundRequestDto.getTranslatedCertificateNo());
        reservationRequestDto.setCustNm(outboundRequestDto.getCustomerName());
        reservationRequestDto.setMpNo(outboundRequestDto.getMobile());
        reservationRequestDto.setVehicleSvcSupport(vehicleSvcSupport);

        return reservationRequestDto;
    }

    private PlaceResponseDto placeToPlaceResponseDto(Place place){

        PlaceResponseDto placeResponseDto = new PlaceResponseDto();
        placeResponseDto.setPlaceId(place.getPlaceId());
        placeResponseDto.setPlaceName(place.getPlaceName());
        placeResponseDto.setPlaceId(place.getPlaceId());
        placeResponseDto.setAddress(place.getAddress());
        placeResponseDto.setIntroduction(place.getIntroduction());
        placeResponseDto.setImageFile(place.getImageFile());

        return placeResponseDto;
    }

    private Map<String, Object> convertPlaceToMap(PlaceDetailRequestDto dto, String imageFile){

        Map<String, Object> customData = new HashMap<>();
        customData.put("placeId", dto.getPlaceId());
        customData.put("placeName", dto.getPlaceName());
        customData.put("address", dto.getAddress());
        customData.put("introduction", dto.getIntroduction());
        customData.put("imageFile", imageFile);
        return customData;
    }

    private String generateMessage(Object obj, String prefix){

        if(obj instanceof RegionDetailRequestDto){
            RegionDetailRequestDto dto = (RegionDetailRequestDto)obj;
            return String.format("%s | %d | %s", prefix, dto.getRegionId(), dto.getRegionName());
        }
        else if(obj instanceof PlaceDetailRequestDto ){
            PlaceDetailRequestDto dto = (PlaceDetailRequestDto) obj;
            return String.format("%s | %s %s %s %s",
                    prefix,
                    nullableString(dto.getPlaceName()),
                    nullableString(dto.getRegionId()),
                    nullableString(dto.getAddress()),
                    nullableString(dto.getIntroduction()));

        }
        else if(obj instanceof RegionRequestDto){
            RegionRequestDto dto = (RegionRequestDto) obj;
            return String.format("%s | 지역 이름 = %s.", prefix, dto.getRegionName());

        }
        else if(obj instanceof OutboundRequestDto){
            OutboundRequestDto dto = (OutboundRequestDto) obj;
            return String.format("%s | PCP-관리 계약 번호 = %s, 상조사 증서 번호 = %s | 계약자 = %s | 전화 번호 = %s | 차량 지원 서비스 문의 여부 = %s",
                    prefix, nullableString(dto.getTranslatedCertificateNo()), nullableString(dto.getFuneralCertificateNo()),
                    nullableString(dto.getCustomerName()), nullableString(dto.getMobile()), nullableString(dto.getVehicleSvcSupport()));

        }
        else if(obj instanceof ReservationFuneralAgentDto){
            ReservationFuneralAgentDto dto = (ReservationFuneralAgentDto)obj;
            return String.format("%s | %s | %s", prefix, nullableString(dto.getFuneralPlace()), nullableString(dto.getFuneralDate()));

        }else if(obj instanceof FavoritePlaceSetRequestDto){
            FavoritePlaceSetRequestDto dto = (FavoritePlaceSetRequestDto) obj;
            return String.format("%s | uid = %d | 장묘지 정보 id = %d", prefix, dto.getUid(), dto.getPlaceId());

        }else if(obj instanceof BoardRequestDto){
            BoardRequestDto dto = (BoardRequestDto)obj;
            return String.format("%s | %s ", prefix, dto.getTitle());

        }else if(obj instanceof FaqCategoryRequestDto){
            FaqCategoryRequestDto dto = (FaqCategoryRequestDto)obj;
            return String.format("%s", dto.getCategoryName());

        }else if(obj instanceof FaqRequestDto){
            FaqRequestDto dto = (FaqRequestDto)obj;
            return String.format("%s", dto.getQuestion());
        }
        return null;
    }

    private void saveReservationResHistory(ReservationRes actions){

        ReservationResHistory history = new ReservationResHistory();
        history.setTranslatedCertificateNo(actions.getTranslatedCertificateNo());
        history.setFuneralCertificateNo(actions.getFuneralCertificateNo());
        history.setFuneralPlace(actions.getFuneralPlace());
        history.setFuneralDate(actions.getFuneralDate());
        history.setUid(actions.getUid());
        history.setReservationType(actions.getReservationType());
        history.setReservationState(actions.getReservationState());

        reservationResHistoryRepository.save(history);
    }

    private void saveReservationHistory(Reservation reservation, List<String> placeNames, String actionType){

        ReservationHistory history = new ReservationHistory();
        history.setTranslatedCertificateNo(reservation.getTranslatedCertificateNo());
        history.setFuneralCertificateNo(reservation.getFuneralCertificateNo());
        history.setUid(reservation.getUid());
        history.setFavoritePlaceNames(placeNames);
        history.setVehicleSvcSupport(reservation.getVehicleSvcSupport());
        history.setActionType(actionType);

        reservationHistoryRepository.save(history);
    }

    private Place findPlaceById(Long placeId, String prefix){
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException(prefix + " Place not found id = " + placeId));
    }

    private Region findRegionById(Long regionId, String prefix){
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException(prefix + " Region not found, id = " + regionId));
    }

    private UsersInfo findUsersInfo(Long uid){
        return usersInfoRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException(" UserInfo not found  = " + uid));

    }

    private Reservation findReservation(String translatedCertificateNo){
        return reservationRepository.findById(translatedCertificateNo)
                .orElseThrow(() -> new IllegalArgumentException(" Reservation not found, id = " + translatedCertificateNo));
    }

    private String nullableString(Object obj){
        return obj != null ? obj.toString() : null;
    }

}
