package com.pcp.funeralsvc.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = ",";  // 구분자를 콤마(,)로 설정

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return String.join(SEPARATOR, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
