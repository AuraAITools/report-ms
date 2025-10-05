package com.aura.core.entities.links;

import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
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
public class SubjectStudentLink {

    private Student student;

    private Subject subject;

}
