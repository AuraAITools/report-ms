package com.aura.core.entities.lessons;

import com.aura.core.entities.links.StudentLessonLink;
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
public class LessonHomeworkCompletion {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private StudentLessonLink studentLessonLink;

    private Integer completion;

}
