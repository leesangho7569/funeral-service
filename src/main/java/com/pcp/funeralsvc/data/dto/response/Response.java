package com.pcp.funeralsvc.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
	
	private ResponseCode responseCode;
	private String responseMessage;
	private Object responseData;
	
	public Response() {
	}
	
	public Response(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.responseMessage = responseCode.getMsg();
	}
	
	public Response(ResponseCode responseCode, Object responseData) {
		this.responseCode = responseCode;
		this.responseMessage = responseCode.getMsg();
		this.responseData = responseData;
	}
}
