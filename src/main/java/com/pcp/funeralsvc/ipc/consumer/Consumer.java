package com.pcp.funeralsvc.ipc.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pcp.funeralsvc.config.AmqpConfiguration;
import com.pcp.funeralsvc.data.entity.ReservationRes;
import com.pcp.funeralsvc.data.repository.ReservationResRepository;
import com.pcp.funeralsvc.ipc.logAndEvent.dto.CustomerEventDto;
import com.pcp.funeralsvc.data.entity.UsersInfo;
import com.pcp.funeralsvc.data.repository.UsersInfoRepository;
import com.pcp.funeralsvc.utils.Json;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.pcp.funeralsvc.config.AmqpConfiguration.ROUTING_KEY_MAP;

@Component
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private UsersInfoRepository usersInfoRepository;
    private ReservationResRepository reservationResRepository;

    @Autowired
    public Consumer(UsersInfoRepository usersInfoRepository, ReservationResRepository reservationResRepository){
        this.usersInfoRepository = usersInfoRepository;
        this.reservationResRepository = reservationResRepository;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = AmqpConfiguration.QUEUE),
            exchange = @Exchange(value = AmqpConfiguration.EXCHANGE, type = ExchangeTypes.TOPIC),
            key = {AmqpConfiguration.PCP_JOINED_KEY, AmqpConfiguration.VAS_JOINED_KEY, AmqpConfiguration.PCP_WITHDRAWN_KEY, AmqpConfiguration.VAS_WITHDRAWN, AmqpConfiguration.LOGGED_IN_KEY})
    )
    public void consumeMessage(@Payload String payload, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String topic){

        for (Map.Entry<String, String> entry : ROUTING_KEY_MAP.entrySet()) {

            if(topic.startsWith(entry.getValue())) {

                try {
                    CustomerEventDto customerEventDto = Json.toObjectJson(payload, CustomerEventDto.class);
                    logger.info("{} | uid = {} | message = {}, customData = {}", entry.getKey().toString(), customerEventDto.getData().getCustomer().getId(), Json.toStringJson(customerEventDto),
                            customerEventDto.getData().getEvent().getCustomData());

                    if(!customerEventDto.getData().getEvent().getStatus().toUpperCase(Locale.ROOT).equals("SUCCESS")) {
                        logger.info("{} | uid = {} | status = {}", entry.getKey().toString(), customerEventDto.getData().getCustomer().getId(), customerEventDto.getData().getEvent().getStatus());
                        return;
                    }

                    //탈퇴
                    if(entry.getValue().equals(AmqpConfiguration.PCP_WITHDRAWN_KEY) || entry.getValue().equals(AmqpConfiguration.VAS_WITHDRAWN)){
                        logger.info("{} | uid = {}", entry.getKey().toString(), customerEventDto.getData().getCustomer().getId());
                        //usersInfoRepository.deleteById(Long.valueOf(customerEventDto.getData().getCustomer().getId()));
                        return;
                    }

                    if(customerEventDto.getData().getCustomer() != null && customerEventDto.getData().getCustomer().getId() != null ) {

                        Long uid = Long.valueOf(customerEventDto.getData().getCustomer().getId());

                        if(entry.getValue().equals(AmqpConfiguration.LOGGED_IN_KEY)){

                            UsersInfo usersInfo = null;
                            Optional<UsersInfo> user = usersInfoRepository.findById(uid);
                            if (user.isPresent()) {
                                usersInfo = user.get();
                            } else {
                                usersInfo = new UsersInfo();
                                usersInfo.setUid(uid);
                            }

                            if (customerEventDto.getData().getEvent().getCustomData().get("customerName") != null) {
                                usersInfo.setCustomerName(customerEventDto.getData().getEvent().getCustomData().get("customerName").toString());
                            }

                            if (customerEventDto.getData().getEvent().getCustomData().get("mobile") != null) {
                                usersInfo.setMobile(customerEventDto.getData().getEvent().getCustomData().get("mobile").toString());
                            }

                            if (customerEventDto.getData().getEvent().getCustomData().get("passAppId") != null) {
                                usersInfo.setPassAppId(customerEventDto.getData().getEvent().getCustomData().get("passAppId").toString());
                            }

                            usersInfoRepository.save(usersInfo);
                            logger.info("{} | {} uid = {} | mobile = {}, passAppId = {}, customerName = {}",
                                    entry.getKey().toString(), user.isPresent() == true ? "기존" : "신규", usersInfo.getUid(),
                                    usersInfo.getMobile(),
                                    usersInfo.getPassAppId(),
                                    usersInfo.getCustomerName());

                        }

                        else if(entry.getValue().equals(AmqpConfiguration.VAS_JOINED_KEY)){

                            if(customerEventDto.getData().getEvent().getCustomData().get("translatedCertificateNo") != null &&
                                    customerEventDto.getData().getEvent().getCustomData().get("funeralCertificateNo") != null) {

                                String translatedCertificateNo = customerEventDto.getData().getEvent().getCustomData().get("translatedCertificateNo").toString();
                                String funeralCertificateNo = customerEventDto.getData().getEvent().getCustomData().get("funeralCertificateNo").toString();

                                ReservationRes actions = null;
                                Optional<ReservationRes> reservationResOptional = reservationResRepository.findById(translatedCertificateNo);
                                if (reservationResOptional.isPresent()) {
                                    actions = reservationResOptional.get();
                                } else {
                                    actions = new ReservationRes();
                                    actions.setTranslatedCertificateNo(translatedCertificateNo);
                                    actions.setFuneralCertificateNo(funeralCertificateNo);
                                }
                                actions.setUid(uid);
                                logger.info("{} | {} uid = {} | translatedCertificateNo = {}, funeralCertificateNo = {}",
                                        entry.getKey().toString(), reservationResOptional.isPresent() == true ? "기존" : "신규", actions.getUid(),
                                        actions.getTranslatedCertificateNo(), actions.getFuneralCertificateNo());
                            }
                            else{
                                logger.error("{} | uid = {} | translatedCertificateNo or funeralCertificateNo null",
                                        entry.getKey().toString(), uid);
                            }

                        }

                    }

                }catch (JsonProcessingException e) {
                    // JSON 변환 실패 시 로그
                    logger.error("Failed to process JSON payload: {}", payload, e);
                }catch (Exception e) {
                    // 기타 예외 처리
                    logger.error("An unexpected error occurred while processing the message", e);
                }
            }
        }

    }
}