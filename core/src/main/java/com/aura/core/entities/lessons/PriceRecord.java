package com.aura.core.entities.lessons;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String courseId;
    private Double price;
    private FREQUENCY frequency;

    public enum FREQUENCY {
        MONTHLY,
        WEEKLY,
        PER_LESSON,
        TOTAL
    }


}
