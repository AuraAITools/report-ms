package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
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
@Table(name = "LessonQuizStudentLessonRegistrationAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lesson_quiz_student_lesson_registration",
                        columnNames = {"lesson_quiz_id", "student_lesson_registration_id"}
                )
        })
public class LessonQuizStudentLessonRegistrationAttachment extends AttachmentTenantAwareBaseEntityTemplate<LessonQuiz, StudentLessonRegistration> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_quiz_id")
    @ToString.Exclude
    private LessonQuiz lessonQuiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_lesson_registration_id")
    @ToString.Exclude
    private StudentLessonRegistration studentLessonRegistration;

    // store results of a student registered under a lesson registered under the lesson's quiz here
    private int score;

    private int maxScore;

    @Override
    public LessonQuiz getFirstEntity() {
        return lessonQuiz;
    }

    @Override
    public StudentLessonRegistration getSecondEntity() {
        return studentLessonRegistration;
    }

    @Override
    public Collection<LessonQuizStudentLessonRegistrationAttachment> getFirstEntityAttachments() {
        return lessonQuiz.getLessonQuizStudentLessonRegistrationAttachments() != null ? lessonQuiz.getLessonQuizStudentLessonRegistrationAttachments() : null;
    }

    @Override
    public Collection<LessonQuizStudentLessonRegistrationAttachment> getSecondEntityAttachments() {
        return studentLessonRegistration.getLessonQuizStudentLessonRegistrationAttachments() != null ? lessonQuiz.getLessonQuizStudentLessonRegistrationAttachments() : null;
    }

    @Override
    public LessonQuizStudentLessonRegistrationAttachment create(LessonQuiz entity1, StudentLessonRegistration entity2) {
        return LessonQuizStudentLessonRegistrationAttachment
                .builder()
                .lessonQuiz(entity1)
                .studentLessonRegistration(entity2)
                .build();
    }
}
