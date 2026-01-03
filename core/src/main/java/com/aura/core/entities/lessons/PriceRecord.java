package com.aura.core.entities.lessons;

import com.aura.core.entities.courses.Course;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceRecord {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Course course;
    private Double price;
    private FREQUENCY frequency;

    public enum FREQUENCY {
        MONTHLY,
        WEEKLY,
        PER_LESSON,
        TOTAL
    }


}
