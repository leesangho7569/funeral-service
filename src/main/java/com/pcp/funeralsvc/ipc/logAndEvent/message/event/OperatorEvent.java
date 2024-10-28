package com.pcp.funeralsvc.ipc.logAndEvent.message.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pcp.funeralsvc.ipc.logAndEvent.message.Actor;
import com.pcp.funeralsvc.ipc.logAndEvent.message.Security;
import jakarta.servlet.http.HttpServletRequest;
import lombok.ToString;
import com.pcp.funeralsvc.ipc.logAndEvent.message.*;

import java.util.HashMap;
import java.util.Map;

@ToString
public class OperatorEvent extends EventMessage {

	public OperatorEvent(HttpServletRequest request) {
		super(new Header());
		this.data = new Data(request);
	}

	@JsonProperty("data")
	public Map<String, Object> serializeData() {
		Map<String, Object> map = new HashMap<>();
		map.put("url", data.getUrl());
		map.put("operator", serializeActor());
		map.put("event", serializeEvent());
		if(data.getSecurity() != null){
			map.put("security", serializeSecurity());
		}
		
		return map;
	}
	
	public Map<String, Object> serializeActor() {
		Actor from = data.getActor();
		Map<String, Object> to = new HashMap<>();
		
		to.put("id", from.getId());
		to.put("ip", from.getIp());
		to.put("agent", from.getId());
		to.put("type", from.getType());
		to.put("name", from.getName());
		to.put("division", from.getDivision());
		
		return to;
	}
	
	public Map<String, Object> serializeSecurity() {
		Security from = data.getSecurity();
		Map<String, Object> to = new HashMap<>();
		
		to.put("target", from.getTarget());
		to.put("type", from.getType());
		
		return to;
	}
}
