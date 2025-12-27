package com.aura.core.entities.links;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
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
public class EducatorCourseLink {
    private Educator educator;

    private Course course;


}
