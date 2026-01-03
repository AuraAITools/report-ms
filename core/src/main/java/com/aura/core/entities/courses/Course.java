package com.aura.core.entities.courses;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.PriceRecord;
import com.aura.core.entities.links.EducatorCourseLink;
import com.aura.core.entities.links.StudentCourseLink;
import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    private Outlet outlet;
    private Level level;
    private Set<SubjectCourseLink> subjectCourseLinks = new HashSet<>();
    private Set<StudentCourseLink> studentCourseLinks = new HashSet<>();
    private Set<EducatorCourseLink> educatorCourseLinks = new HashSet<>();
    private Set<Lesson> lessons = new HashSet<>();

    public enum LESSON_FREQUENCY {
        WEEKLY,
        FORTNIGHTLY,
        MONTHLY
    }

}
