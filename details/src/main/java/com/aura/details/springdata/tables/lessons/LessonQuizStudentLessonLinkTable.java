package com.aura.details.springdata.tables.lessons;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.links.StudentLessonLinkTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * a lesson quiz registration from a student registered under a lesson registered for this quiz
 */
@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonQuizStudentLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lesson_quiz_student_lesson_link",
                        columnNames = {"lesson_quiz_id", "student_lesson_link_id"}
                )
        })
public class LessonQuizStudentLessonLinkTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_quiz_id")
    @ToString.Exclude
    private LessonQuizTable lessonQuizTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_lesson_link_id")
    @ToString.Exclude
    private StudentLessonLinkTable studentLessonLink;

    // store results of a student registered under a lesson registered under the lesson's quiz here
    private int score;

    private int maxScore;
}
