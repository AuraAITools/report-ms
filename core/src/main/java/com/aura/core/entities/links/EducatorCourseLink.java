package com.aura.core.entities.links;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EducatorCourseLink {
    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Educator educator;

    private Course course;


}
