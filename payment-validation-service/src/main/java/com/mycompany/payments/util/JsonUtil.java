package com.mycompany.payments.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility for JSON and Java object conversions using Jackson's {@link ObjectMapper}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JsonUtil {

    private final ObjectMapper objectMapper;

    /**
     * Converts a JSON string into the requested Java type.
     *
     * @param jsonString JSON payload to be converted
     * @param type target Java type
     * @param <T> target type parameter
     * @return deserialized Java object, or {@code null} when conversion fails
     */
    public <T> T convertJsonToObject(String jsonString, Class<T> type) {
        if (jsonString == null || type == null) {
            log.warn("Unable to convert JSON to object because jsonString or type is null");
            return null;
        }
        try {
            T result = objectMapper.readValue(jsonString, type);
            log.debug("Converted JSON payload to {}", type.getSimpleName());
            return result;
        } catch (Exception e) {
            log.error("Failed to convert JSON to {}", type.getSimpleName(), e);
            return null;
        }
    }

    /**
     * Converts a Java object into its JSON string representation.
     *
     * @param obj Java object to serialize
     * @return serialized JSON string, or {@code null} when conversion fails
     */
    public String convertObjectToJson(Object obj) {
        if (obj == null) {
            log.warn("Unable to convert object to JSON because the input object is null");
            return null;
        }
        try {
            String json = objectMapper.writeValueAsString(obj);
            log.debug("Converted {} to JSON", obj.getClass().getSimpleName());
            return json;
        } catch (JsonProcessingException e) {
            log.error("Failed to convert {} to JSON", obj.getClass().getSimpleName(), e);
            return null;
        }
    }
}
