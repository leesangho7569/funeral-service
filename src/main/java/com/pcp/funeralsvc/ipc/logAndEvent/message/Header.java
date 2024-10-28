package com.pcp.funeralsvc.ipc.logAndEvent.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Getter @Setter
@ToString
public class Header {
	private String timestamp;
	private Source source;
	
	public Header() {
		this.source = Source.getInstance();
	}
	
	@Getter @Setter
	@ToString
	public static class Source {
		@JsonIgnore
		private static Source source;
		
		private final String service = "funeral-service";
		private String host;
		private String ip;
		
		private Source() {
		}
		
		private static Source getInstance() {
			if(source == null) {
				Source instance = new Source();
				try {
					instance.setHost(InetAddress.getLocalHost().toString());
					instance.setIp(InetAddress.getLocalHost().getHostAddress());
					
					source = instance;
				} catch(UnknownHostException e) {
					throw new RuntimeException(e);
				}
			}
			
			return source;
		}
	}
	
}
