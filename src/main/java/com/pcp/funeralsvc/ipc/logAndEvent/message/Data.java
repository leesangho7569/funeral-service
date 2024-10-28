package com.pcp.funeralsvc.ipc.logAndEvent.message;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;

@Setter
public class Data {
	private String url;
	private Actor actor;
	private Event event;
	private Security security;

	public Data(HttpServletRequest request) {
		url = request.getRequestURI();
		actor = new Actor(request);
		event = new Event();
		security = new Security();
	}
	
	public String getUrl() {
		return url;
	}
	
	public Actor getActor() {
		return actor;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public Security getSecurity() {
		return security;
	}
	
}
