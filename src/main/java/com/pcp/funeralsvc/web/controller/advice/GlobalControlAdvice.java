package com.pcp.funeralsvc.web.controller.advice;

import com.pcp.funeralsvc.data.dto.response.Response;
import com.pcp.funeralsvc.data.dto.response.ResponseCode;

import com.pcp.funeralsvc.exception.LogicalException;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControlAdvice {

    private final Logger logger = LoggerFactory.getLogger(GlobalControlAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentExceptions(
            HttpServletRequest request,
            BindingResult bindingResult,
            MethodArgumentNotValidException e
    ){
//       Map<String, String> errors = new HashMap<>();
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", ", request.getRequestURI() + " | ", ""));

//        e.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage())
//        );

        logger.error(errorMsg);
        return new Response(ResponseCode.F00001, errorMsg);
    }

    @ExceptionHandler(value = {IOException.class})
    public Response handleIOException(Exception e){

        logger.error(e.getMessage());
        return new Response(ResponseCode.E00001);
    }

    @ExceptionHandler(value = {DuplicateRequestException.class})
    public Response handleDuplicateKeyException(DuplicateRequestException e){

        logger.error(e.getMessage());
        return new Response(ResponseCode.F00003);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Response handleIllegalArgumentException(IllegalArgumentException e){
        logger.error(e.getMessage());
        return new Response(ResponseCode.F02404);
    }

    @ExceptionHandler(value = {Exception.class})
    public Response handleException(Exception e){
        e.printStackTrace();
        logger.error(e.getMessage());
        return new Response(ResponseCode.E00001);
    }

    @ExceptionHandler(value = {LogicalException.class})
    public Response handleLogicalException(LogicalException e) {
        logger.error(e.getMessage());
        e.printStackTrace();

        return new Response(e.getResponseCode());
    }

}
