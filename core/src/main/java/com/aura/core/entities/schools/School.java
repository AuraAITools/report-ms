package com.aura.core.entities.schools;

import com.aura.core.entities.students.Student;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class School {
    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String name;
    private SchoolCategory schoolCategory;
    private Set<Student> students;
    private String tenantId;
}
