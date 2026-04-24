package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "LessonQuizStudentLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lesson_quiz_student_lesson_link",
                        columnNames = {"lesson_quiz_id", "student_lesson_link_id"}
                )
        })
public class LessonQuizStudentLessonLinkModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_quiz_id")
    @ToString.Exclude
    private LessonQuizModel lessonQuizTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_lesson_link_id")
    @ToString.Exclude
    private StudentLessonLinkModel studentLessonLink;

    // store results of a student registered under a lesson registered under the lesson's quiz here
    private int score;

    private int maxScore;
}
