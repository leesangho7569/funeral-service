package com.pcp.funeralsvc.ipc.logAndEvent.message;

import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.data.dto.response.ResponseCode;
import com.pcp.funeralsvc.ipc.logAndEvent.message.event.CustomerEvent;
import com.pcp.funeralsvc.ipc.logAndEvent.message.event.CustomerEventBuilder;
import com.pcp.funeralsvc.ipc.logAndEvent.message.event.EventMessage;
import com.pcp.funeralsvc.ipc.logAndEvent.message.event.OperatorEventBuilder;
import com.pcp.funeralsvc.ipc.logAndEvent.message.log.GeneralLogBuilder;
import com.pcp.funeralsvc.ipc.producer.Producer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Map;


public class LoggingContext {

	public static String general_log_routing_key = "log.application.created";
	private static ThreadLocal<GeneralLogBuilder> generalLoggerBuilder = new ThreadLocal<>();
	private static ThreadLocal<CustomerEventBuilder> customerEventBuilder = new ThreadLocal<>();
	private static ThreadLocal<OperatorEventBuilder> operatorEventBuilder = new ThreadLocal<>();

	private static Producer producer;
	public static void initialize(HttpServletRequest request, Producer producerBean) {
		initGeneralLog();
		initCustomerEvent(request);
		initOperatorEvent(request);
		producer = producerBean;
	}
	
	public static void remove() {
		generalLoggerBuilder.remove();
		customerEventBuilder.remove();
		operatorEventBuilder.remove();
	}
	
	public static void initGeneralLog() {

		generalLoggerBuilder.set(new GeneralLogBuilder(new Header()));
	}
	public static GeneralLogBuilder general() {
		GeneralLogBuilder builder = generalLoggerBuilder.get();
		if(builder == null) {
			throw new IllegalStateException("No GeneralLogBuilder set");
		}

		return builder;
	}

	public static void initCustomerEvent(HttpServletRequest request) {
		customerEventBuilder.set(new CustomerEventBuilder(request));

	}
	
	public static void initOperatorEvent(HttpServletRequest request) {
		operatorEventBuilder.set(new OperatorEventBuilder(request));

	}

	public static CustomerEventBuilder customer() {
		CustomerEventBuilder builder = customerEventBuilder.get();
		if(builder == null) {
			throw new IllegalStateException("No CustomerEventBuilder set");
		}
		
		return builder;
	}
	
	public static OperatorEventBuilder operator() {
		OperatorEventBuilder builder = operatorEventBuilder.get();
		if(builder == null) {
			throw new IllegalStateException("No OperatorEventBuilder set");
		}
		
		return builder;
	}

	public static void sendCustomerEvent(Response response, String eventName, String routingKey) {
		if(response == null || StringUtils.isAnyBlank(eventName, routingKey)) {
			return;
		}

		CustomerEvent customerEvent = customer()
				.eventName(eventName)
				.status(response.getResponseCode() == ResponseCode.S00000 ? "SUCCESS" : "FAIL")
				.resCode(response.getResponseCode().toString())
				.resMessage(response.getResponseMessage())
				.eventComplete();

		sendEvent(customerEvent, routingKey);
	}

	public static void sendCustomerEvent(Response response, String eventName, String routingKey, @Nullable Map<String, Object> customData) {
		if(response == null || StringUtils.isAnyBlank(eventName, routingKey)) {
			return;
		}

		CustomerEvent customerEvent = customer()
				.eventName(eventName)
				.status(response.getResponseCode() == ResponseCode.S00000 ? "SUCCESS" : "FAIL")
				.resCode(response.getResponseCode().toString())
				.resMessage(response.getResponseMessage())
				.customData(customData)
				.eventComplete();

		sendEvent(customerEvent, routingKey);
	}

	private static void sendEvent(EventMessage eventMessage, String routingKey) {
		producer.publish(routingKey, eventMessage);
	}

}
