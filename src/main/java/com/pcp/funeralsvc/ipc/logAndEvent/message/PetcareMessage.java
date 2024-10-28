package com.pcp.funeralsvc.ipc.logAndEvent.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter @Setter
@ToString
public class PetcareMessage {
	
	@JsonIgnore
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@JsonUnwrapped
	private Header header;

	public PetcareMessage(Header header) {
		this.header = header;
	}
	
}
