package com.pcp.funeralsvc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class Json {

    public static String toStringJson(Object obj) throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return jsonMapper.writeValueAsString(obj);
    }

    public static <T> T toObjectJson(String json, Class<T> type) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.readValue(json, type);
    }

    public static <T> T toObjectConvert(Object json, Class<T> type) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.convertValue(json, type);
    }


}