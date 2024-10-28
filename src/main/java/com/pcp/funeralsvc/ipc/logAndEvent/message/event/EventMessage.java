package com.pcp.funeralsvc.ipc.logAndEvent.message.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.pcp.funeralsvc.ipc.logAndEvent.message.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Setter
@ToString
public class EventMessage extends PetcareMessage {
	protected Data data;
	//protected Event event = new Event();

	@JsonIgnore
	protected String routingKey;

	public EventMessage(Header header) {
		super(header);
	}

	@JsonIgnore
	public Data getData() {
		return data;
	}

	protected void complete(HttpServletResponse response) {
		getHeader().setTimestamp(LocalDateTime.now().format(PetcareMessage.FORMATTER));
		
		Event event = this.data.getEvent();
		event.setEndTimeMillis(System.currentTimeMillis());
		event.setDuration(
				this.data.getEvent().getEndTimeMillis() - this.data.getEvent().getStartTimeMillis()
		);
		event.setResCode(response == null ? "": Integer.toString(response.getStatus()));
	}
	
	protected Map<String, Object> serializeEvent() {
		Event event = data.getEvent();
		Map<String, Object> to = new HashMap<>();

		to.put("name", event.getName());
		to.put("resCode", event.getResCode());
		to.put("resMessage", event.getResMessage());
		to.put("status", event.getStatus());
		to.put("duration", event.getDuration());
		to.put("customData", event.getCustomData());
		
		return to;
	}

	@JsonIgnore
	public String getRoutingKey(){ return routingKey; }

}
