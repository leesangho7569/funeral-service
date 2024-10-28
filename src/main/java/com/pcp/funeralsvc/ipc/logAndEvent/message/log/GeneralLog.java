package com.pcp.funeralsvc.ipc.logAndEvent.message.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pcp.funeralsvc.ipc.logAndEvent.message.Header;
import com.pcp.funeralsvc.ipc.logAndEvent.message.PetcareMessage;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter
@ToString
public class GeneralLog extends PetcareMessage {
	private String key;
	private Level level;
	private String msg;

	public GeneralLog() {
		super(new Header());
	}
	
	@JsonIgnore
	public String getKey() {
		return key;
	}
	
	@JsonIgnore
	public Level getLevel() {
		return level;
	}
	
	@JsonIgnore
	public String getMsg() {
		return msg;
	}
	
	@JsonProperty("data")
	public Map<String, Object> getData() {
		Map<String, Object> data = new HashMap<>();
		data.put("key", key);
		data.put("level", level);
		data.put("msg", msg);
		
		return data;
	}
	
}
