package com.aura.core.entities.levels;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.links.LevelEducatorLink;
import com.aura.core.entities.links.SubjectLevelLink;
import com.aura.core.entities.schools.SchoolCategory;
import com.aura.core.entities.students.Student;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Level {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String name;

    private SchoolCategory category;

    private List<Student> students;
    private Set<SubjectLevelLink> subjectLevelLinks = new HashSet<>();
    private Set<LevelEducatorLink> levelEducatorLinks = new HashSet<>();
    private Set<Course> courses = new HashSet<>();

    private String tenantId;
}
