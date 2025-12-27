package com.aura.app.cursor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.graphql.data.pagination.CursorStrategy;

public class CleanJsonKeysetCursorStrategy implements CursorStrategy<Map<String, Object>> {

    private final ObjectMapper objectMapper;

    public CleanJsonKeysetCursorStrategy() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public boolean supports(Class<?> targetType) {
        return Map.class.isAssignableFrom(targetType);
    }

    @Override
    public String toCursor(Map<String, Object> keys) {
        try {
            // Convert complex objects to simple strings to avoid type metadata
            Map<String, Object> cleanKeys = new HashMap<>();
            keys.forEach((key, value) -> {
                if (value instanceof UUID) {
                    cleanKeys.put(key, value.toString());
                } else if (value instanceof Instant) {
                    cleanKeys.put(key, value.toString());
                } else {
                    cleanKeys.put(key, value);
                }
            });

            String json = objectMapper.writeValueAsString(cleanKeys);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize keyset", e);
        }
    }

    @Override
    public Map<String, Object> fromCursor(String cursor) {
        try {
            // Decode from Base64
            byte[] decodedBytes = Base64.getDecoder().decode(cursor);
            String json = new String(decodedBytes, StandardCharsets.UTF_8);

            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
            };
            Map<String, Object> keys = objectMapper.readValue(json, typeRef);

            // Convert string values back to proper types
            Map<String, Object> convertedKeys = new HashMap<>();
            keys.forEach((key, value) -> {
                if (value instanceof String) {
                    String stringValue = (String) value;

                    // Convert back to UUID if it looks like one
                    if (looksLikeUuid(stringValue)) {
                        try {
                            convertedKeys.put(key, UUID.fromString(stringValue));
                            return;
                        } catch (IllegalArgumentException e) {
                            // Not a UUID, continue
                        }
                    }

                    // Convert back to Instant if it looks like one
                    if (looksLikeInstant(stringValue)) {
                        try {
                            convertedKeys.put(key, Instant.parse(stringValue));
                            return;
                        } catch (Exception e) {
                            // Not an Instant, continue
                        }
                    }
                }

                convertedKeys.put(key, value);
            });

            return convertedKeys;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize keyset from cursor: " + cursor, e);
        }
    }

    private boolean looksLikeUuid(String value) {
        return value != null && value.matches(
                "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
        );
    }

    private boolean looksLikeInstant(String value) {
        return value != null && value.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*");
    }
}