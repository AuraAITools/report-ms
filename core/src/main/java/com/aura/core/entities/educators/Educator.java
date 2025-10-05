package com.aura.core.entities.educators;

import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.EducatorCourseLink;
import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.entities.links.LevelEducatorLink;
import com.aura.core.entities.links.OutletEducatorLink;
import com.aura.core.entities.links.SubjectEducatorLink;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Educator {

    private String name;
    private LocalDate startDate;
    private LocalDate dateOfBirth;
    private String email;
    private EMPLOYMENT_TYPE employmentType;
    private Set<EducatorCourseLink> educatorCourseLinks = new HashSet<>();
    private Set<SubjectEducatorLink> subjectEducatorLinks = new HashSet<>();
    private Set<OutletEducatorLink> outletEducatorLinks = new HashSet<>();
    private Set<LevelEducatorLink> levelEducatorLinks = new HashSet<>();
    private Set<EducatorLessonLink> educatorLessonLinks = new HashSet<>();
    private Set<AccountEducatorLink> accountEducatorLinks = new HashSet<>();

    public enum EMPLOYMENT_TYPE {
        FULL_TIME,
        PART_TIME
    }

}
