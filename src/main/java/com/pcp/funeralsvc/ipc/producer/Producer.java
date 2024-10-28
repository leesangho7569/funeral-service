package com.pcp.funeralsvc.ipc.producer;

import com.pcp.funeralsvc.config.AmqpConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final RabbitTemplate template;

    @Autowired
    public Producer(RabbitTemplate template){
        this.template = template;
    }

    public void publish(String routing_key, Object message){

        template.convertAndSend(AmqpConfiguration.EXCHANGE, routing_key, message);
    }

}
