package com.aura.core.entities.courses;

import com.aura.core.entities.lessons.PriceRecord;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private LESSON_FREQUENCY lessonFrequency;

    private PriceRecord priceRecord;
    private String name;
    private Integer maxSize;
    private Instant courseStartTimestamptz;
    private Instant courseEndTimestamptz;
    private String outletId;
    private String levelId;

    public enum LESSON_FREQUENCY {
        WEEKLY,
        FORTNIGHTLY,
        MONTHLY
    }

}
