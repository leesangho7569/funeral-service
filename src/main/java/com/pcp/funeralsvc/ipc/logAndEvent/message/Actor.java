package com.pcp.funeralsvc.ipc.logAndEvent.message;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Actor {
	private String id;
	private String ip;
	private String agent;
	private String name;
	private String division;
	private String type;
	
	public Actor(HttpServletRequest request) {
		
		// TODO
		this.id = request.getHeader("");
		this.ip = request.getHeader("");
		this.agent = request.getHeader("");
	}
}
