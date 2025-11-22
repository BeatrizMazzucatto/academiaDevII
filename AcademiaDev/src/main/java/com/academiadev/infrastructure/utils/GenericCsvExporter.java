package com.academiadev.infrastructure.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericCsvExporter {
    
    public static <T> String exportToCsv(List<T> data, List<String> selectedFields) {
        if (data.isEmpty()) {
            return "";
        }
        
        Class<?> clazz = data.get(0).getClass();
        List<String> validFields = validateFields(clazz, selectedFields);
        
        StringBuilder csv = new StringBuilder();
        
        csv.append(String.join(",", validFields)).append("\n");
        
        for (T item : data) {
            List<String> values = new ArrayList<>();
            for (String fieldName : validFields) {
                values.add(getFieldValue(item, fieldName));
            }
            csv.append(String.join(",", values)).append("\n");
        }
        
        return csv.toString();
    }
    
    private static List<String> validateFields(Class<?> clazz, List<String> selectedFields) {
        List<String> validFields = new ArrayList<>();
        List<String> availableFields = getAvailableFields(clazz);
        
        for (String field : selectedFields) {
            if (availableFields.contains(field)) {
                validFields.add(field);
            }
        }
        
        return validFields.isEmpty() ? availableFields : validFields;
    }
    
    private static List<String> getAvailableFields(Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        
        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && method.getParameterCount() == 0) {
                String fieldName = methodName.substring(3);
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                fields.add(fieldName);
            }
        }
        
        return fields;
    }
    
    private static <T> String getFieldValue(T item, String fieldName) {
        try {
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method method = item.getClass().getMethod(methodName);
            Object value = method.invoke(item);
            return value != null ? value.toString().replace(",", ";") : "";
        } catch (Exception e) {
            try {
                Field field = item.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(item);
                return value != null ? value.toString().replace(",", ";") : "";
            } catch (Exception ex) {
                return "";
            }
        }
    }
}

