package com.aura.app.mappers;

import java.time.Instant;
import org.mapstruct.Mapper;

@Mapper
public interface DateToEpochMapper {
    // MapStruct will use this method to convert Instant -> Long (epoch millis)
    default Long map(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}
