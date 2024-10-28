package com.pcp.funeralsvc.ipc.logAndEvent.message.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

import java.util.Map;

public class OperatorEventBuilder {
	private final OperatorEvent operatorEvent;
	
	public OperatorEventBuilder(HttpServletRequest request) {
		operatorEvent = new OperatorEvent(request);

		eventStart();
	}
	private void eventStart() {
		operatorEvent.getData().getEvent().setStartTimeMillis(System.currentTimeMillis());
	}

	public OperatorEventBuilder name(String name) {
		this.operatorEvent.getData().getActor().setName(name);
		
		return this;
	}
	
	public OperatorEventBuilder division(String division) {
		this.operatorEvent.getData().getActor().setDivision(division);
		
		return this;
	}
	
	public OperatorEventBuilder type(String type) {
		this.operatorEvent.getData().getActor().setType(type);
		
		return this;
	}

	public OperatorEventBuilder customData(Map<String, Object> customData) {
		this.operatorEvent.getData().getEvent().setCustomData(customData);

		return this;
	}
	
	public OperatorEvent completeEvent(@Nullable HttpServletResponse response) {
		operatorEvent.complete(response);
		
		return build();
	}
	
	private OperatorEvent build() {
		return operatorEvent;
	}
}
