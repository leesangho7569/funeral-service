package com.pcp.funeralsvc.config;

import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@EnableRabbit
public class AmqpConfiguration {

    private final Logger logger = LoggerFactory.getLogger(AmqpConfiguration.class);

    public static final String QUEUE = "q_for_funeral_service";
    public static final String EXCHANGE = "amq.topic";
    public static final String PCP_JOINED_KEY = "customer.membership.pcp-joined";
    public static final String VAS_JOINED_KEY = "customer.membership.vas-joined";
    public static final String VAS_WITHDRAWN = "customer.membership.vas-withdrawn";
    public static final String PCP_WITHDRAWN_KEY = "customer.membership.pcp-withdrawn";
    public static final String LOGGED_IN_KEY = "customer.access.logged-in";

    public static final Map<String, String> ROUTING_KEY_MAP = new HashMap<>();
    static {
        ROUTING_KEY_MAP.put("pcp-가입", PCP_JOINED_KEY);
        ROUTING_KEY_MAP.put("vas-가입", VAS_JOINED_KEY);
        ROUTING_KEY_MAP.put("vas-해지", VAS_JOINED_KEY);
        ROUTING_KEY_MAP.put("pcp-탈퇴", PCP_WITHDRAWN_KEY);
        ROUTING_KEY_MAP.put("로그인", LOGGED_IN_KEY);
    }

    //customer.membership.pcp-terms-consent-updated
    //customer.membership.pcp-joined	14
    //customer.membership.pcp-terms-consent-updated	14
    //customer.membership.pcp-withdrawn	14
    //customer.membership.vas-joined	14
    //customer.membership.vas-terms-agreed	14
    //customer.membership.vas-withdrawn	14
    //customer.membership.payment-history-retrieved	14
    //customer.membership.refund-amount-calculated

    //customer.access.logged-in

    @Value("${rabbitmq_amqp.url}")
    private String url;

    @Value("${rabbitmq_amqp.username}")
    private String username;

    @Value("${rabbitmq_amqp.password}")
    private String password;

    @Bean
    public CachingConnectionFactory connectionFactory(){

        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses(url);
        factory.setUsername(username);
        factory.setPassword(password);
        logger.info("connectionFactory | {} {} {} completed.", url, username,password);

        //난수 숫자 랜덤 4자리 수
        factory.setConnectionNameStrategy(connectionFactory -> "funeral_service-" + (new Random().nextInt(9000) + 1000));

        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory funeral_connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(funeral_connectionFactory);
        factory.setPrefetchCount(1); //set prefetch count to 10
        return factory;
    }

    /*
    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public List<Binding> binding(Queue queue, TopicExchange exchange){
        return Arrays.asList(BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY1),
                BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY2));
    }

     */

    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory funeral_connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(funeral_connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
