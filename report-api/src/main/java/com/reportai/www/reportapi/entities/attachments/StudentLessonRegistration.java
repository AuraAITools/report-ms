package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.LessonTopicProficiency;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonHomeworkCompletion;
import com.reportai.www.reportapi.entities.lessons.LessonParticipationReview;
import com.reportai.www.reportapi.entities.lessons.LessonPlan;
import com.reportai.www.reportapi.entities.lessons.LessonQuizStudentLessonRegistrationAttachment;
import com.reportai.www.reportapi.entities.lessons.StudentLessonAttendance;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentLessonRegistrations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_lesson",
                        columnNames = {"student_id", "lesson_id"}
                )
        })
public class StudentLessonRegistration extends AttachmentTenantAwareBaseEntityTemplate<Student, Lesson, StudentLessonRegistration> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonPlan lessonPlan;

    @OneToOne(mappedBy = "studentLessonRegistration", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonHomeworkCompletion lessonHomeworkCompletion;

    @OneToOne(mappedBy = "studentLessonRegistration", cascade = CascadeType.ALL)
    @ToString.Exclude
    private StudentLessonAttendance studentLessonAttendance;

    @OneToOne(mappedBy = "studentLessonRegistration", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonParticipationReview lessonParticipationReview;

    @OneToMany(mappedBy = "studentLessonRegistration", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<LessonQuizStudentLessonRegistrationAttachment> lessonQuizStudentLessonRegistrationAttachments = new HashSet<>();

    @OneToOne(mappedBy = "studentLessonRegistration", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonTopicProficiency lessonTopicProficiency;

    @Override
    public Student getFirstEntity() {
        return student;
    }

    @Override
    public Lesson getSecondEntity() {
        return lesson;
    }

    @Override
    public Collection<StudentLessonRegistration> getFirstEntityAttachments() {
        return student != null ? student.getStudentLessonRegistrations() : null;
    }

    @Override
    public Collection<StudentLessonRegistration> getSecondEntityAttachments() {
        return lesson != null ? lesson.getStudentLessonRegistrations() : null;
    }

    @Override
    public StudentLessonRegistration create(Student entity1, Lesson entity2) {
        return StudentLessonRegistration
                .builder()
                .student(entity1)
                .lesson(entity2)
                .build();
    }

}
