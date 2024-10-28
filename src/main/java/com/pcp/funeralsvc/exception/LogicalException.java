package com.pcp.funeralsvc.exception;

import com.pcp.funeralsvc.data.dto.response.ResponseCode;

public class LogicalException extends RuntimeException{

    private ResponseCode responseCode;

    public  LogicalException() { super(); }

    public LogicalException(String message) { super(message);}

    public LogicalException(ResponseCode responseCode){
        super(responseCode.getMsg());
        this.responseCode = responseCode;
    }
    public LogicalException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.getMsg(), cause);
        this.responseCode = responseCode;
    }
    public LogicalException(ResponseCode responseCode, Object... args){

        super(responseCode.getMsg());
        this.responseCode = responseCode;
    }
    public ResponseCode getResponseCode(){
        return responseCode;
    }


}
