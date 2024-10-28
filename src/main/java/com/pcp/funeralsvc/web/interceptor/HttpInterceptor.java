package com.pcp.funeralsvc.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcp.funeralsvc.ipc.logAndEvent.message.LoggingContext;
import com.pcp.funeralsvc.ipc.producer.Producer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);
    private final static String LINE = "\r\n";
    private Producer producer;

    @Autowired
    public HttpInterceptor(Producer producer){
        this.producer = producer;
    }

//    @Autowired
//    ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        LoggingContext.initialize(request, producer);

        String message = " [" + request.getRequestURI() + " --> REQ(" + request.getMethod() + ")" +" ]" + LINE +
                "===============================================================================================================" +
                LINE;

        logger.info(message);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        request.setAttribute("executeTime", executeTime);

        String message = " [" + request.getRequestURI() + " <-- " + " RES(" + response.getStatus() + ")" +" ]"
                + " execute time: " + executeTime + "ms" + LINE +
                "===============================================================================================================" +
                LINE;

        logger.info(message);

        if(request.getRequestURI().matches("/pcp/funeral/v1/operator/funeral/place-detail-update")) {

            producer.publish("operator.funeral.place-detail-updated", LoggingContext.operator().completeEvent(response));
        }else if(request.getRequestURI().matches("/pcp/funeral/v1/customer/funeral-complete")){
            // TODO customer-event 정의 이후
            //producer.publish("customer.funeral.complete", LoggingContext.customer().completeEvent(response));
        }

        LoggingContext.remove();
    }
}
