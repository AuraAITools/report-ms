package com.reportai.www.reportapi.entities.analytics;

import com.reportai.www.reportapi.entities.Lesson;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.base.AuditableEntity;
import com.reportai.www.reportapi.entities.embeddables.LessonStudentCompositeKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonHomeworkCompletions")
public class LessonHomeworkCompletion extends AuditableEntity {

    @EmbeddedId
    private LessonStudentCompositeKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("lessonId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    public LessonHomeworkCompletion forLesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public LessonHomeworkCompletion forStudent(Student student) {
        this.setStudent(student);
        return this;
    }

    private int rating;

}
