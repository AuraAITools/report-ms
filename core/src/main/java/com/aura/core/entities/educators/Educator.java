package com.aura.core.entities.educators;

import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Educator {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String name;
    private LocalDate startDate;
    private LocalDate dateOfBirth;
    private String email;
    private EMPLOYMENT_TYPE employmentType;
    private @Nullable String profileImageKey;

    public enum EMPLOYMENT_TYPE {
        FULL_TIME,
        PART_TIME
    }

}
