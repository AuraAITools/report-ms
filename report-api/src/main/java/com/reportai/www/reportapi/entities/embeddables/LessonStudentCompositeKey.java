package com.reportai.www.reportapi.entities.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LessonStudentCompositeKey implements Serializable {

    @Column(name = "student_id")
    private UUID studentId;

    @Column(name = "lesson_id")
    private UUID lessonId;

}
