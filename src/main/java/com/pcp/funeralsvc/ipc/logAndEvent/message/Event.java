package com.pcp.funeralsvc.ipc.logAndEvent.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter @Setter
@ToString
public class Event {
	private String name;
	private String status;
	private String resCode;
	private String resMessage;
	private long duration;
	private Map<String, Object> customData;
	
	@JsonIgnore
	private long startTimeMillis;
	@JsonIgnore
	private long endTimeMillis;
}
