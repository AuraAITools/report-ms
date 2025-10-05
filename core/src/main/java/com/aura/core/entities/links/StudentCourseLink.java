package com.aura.core.entities.links;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.students.Student;
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
public class StudentCourseLink {
    private Student student;

    private Course course;

}
