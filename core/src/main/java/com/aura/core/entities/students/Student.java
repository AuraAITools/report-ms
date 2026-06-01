package com.aura.core.entities.students;

import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String name;

    private String email;

    private LocalDate dateOfBirth;

    private String schoolId;

    private String levelId;

    private @Nullable String profileImageKey;
}
