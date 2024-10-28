package com.pcp.funeralsvc.ipc.logAndEvent.message.event;

import com.pcp.funeralsvc.ipc.logAndEvent.message.Header;
import com.pcp.funeralsvc.ipc.logAndEvent.message.PetcareMessage;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Map;

public class CustomerEventBuilder {
	
	private CustomerEvent customerEvent;
	
	public CustomerEventBuilder(HttpServletRequest request) {
		customerEvent = new CustomerEvent(request);
		eventStart();
	}

	private void eventStart() {
		customerEvent.getData().getEvent().setStartTimeMillis(System.currentTimeMillis());
	}

	public CustomerEventBuilder eventName(String eventName) {
		this.customerEvent.getData().getEvent().setName(eventName);
		
		return this;
	}
	
	public CustomerEventBuilder status(String status) {
		this.customerEvent.getData().getEvent().setStatus(status);
		
		return this;
	}

	public CustomerEventBuilder resCode(String resCode) {
		this.customerEvent.getData().getEvent().setResCode(resCode);
		
		return this;
	}
	
	public CustomerEventBuilder resMessage(String resMessage) {
		this.customerEvent.getData().getEvent().setResMessage(resMessage);
		
		return this;
	}
	
	public CustomerEventBuilder customData(Map<String, Object> customData) {
		this.customerEvent.getData().getEvent().setCustomData(customData);
		
		return this;
	}


	public CustomerEventBuilder id(String id){
		this.customerEvent.getData().getActor().setId(id);
		return this;
	}

	public CustomerEvent completeEvent(@Nullable HttpServletResponse response) {

//		customerEvent.getHeader().setTimestamp(LocalDateTime.now().format(PetcareMessage.FORMATTER));

		customerEvent.complete(response);
		
		return build();
	}

	public CustomerEvent eventComplete(){
		return completeEvent(null);
	}


	private CustomerEvent build() {
		return customerEvent;
	}
	
}
