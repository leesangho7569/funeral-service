package com.pcp.funeralsvc.web.external;

import com.pcp.funeralsvc.data.dto.requestToAgent.ReservationRequestDto;
import com.pcp.funeralsvc.data.dto.response.Response;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.Optional;

@Component
public class HttpClient {

    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private HttpComponentsClientHttpRequestFactory factory;

    @Value("${funeral.agent.url}")
    private String agentUrl;

    @Value("${funeral.agent.reservation-path}")
    private String reservationPath;

    @Value("${user-service.url}")
    private String useServiceUrl;

    @Value("${user-service.userinfo-path}")
    private String userServicePath;

    @Value("${funeral.agent.username}")
    private String fu_userName;
    @Value("${funeral.agent.password}")
    private String fu_passWord;

    private String authHeader;

    @Autowired
    public HttpClient() {
    }

    @PostConstruct
    public void init(){
        logger.info("url = {}, path = {}", agentUrl, reservationPath);

        String auth = fu_userName + ":" + fu_passWord;
        String encoderAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        authHeader = "Basic " + encoderAuth;

    }

    public Optional<Response> sendUserInfo(String translatedCertificateNo){

        URI uri = UriComponentsBuilder
                .fromUriString(useServiceUrl)
                .path(userServicePath)
                .queryParam("translatedCertificateNo", translatedCertificateNo)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>("", headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Response.class);

        Response responseDto = responseEntity.getBody();

        return Optional.of(responseDto);
    }
    public Optional<Response> sendReservationRequest(ReservationRequestDto dto){

        URI uri = UriComponentsBuilder
                .fromUriString(agentUrl)
                .path(reservationPath)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);

        HttpEntity<?> requestEntity = new HttpEntity<>(dto, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                Response.class);

//        ResponseEntity<ReservationResponseDto>  responseEntity = restTemplate.postForEntity(uri, dto, ReservationResponseDto.class);
//        ResponseFromAgentDto reservationResponseDto = responseEntity.getBody();
        Response reservationResponseDto = responseEntity.getBody();

        return Optional.of(reservationResponseDto);
    }

    public <T> Optional<Response> sendFuAgentRequest(T dto){

        URI uri = UriComponentsBuilder
                .fromUriString(agentUrl)
                .path(reservationPath)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);

        HttpEntity<?> requestEntity = new HttpEntity<>(dto, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                Response.class);

        Response reservationResponseDto = responseEntity.getBody();

        return Optional.of(reservationResponseDto);
    }


}
