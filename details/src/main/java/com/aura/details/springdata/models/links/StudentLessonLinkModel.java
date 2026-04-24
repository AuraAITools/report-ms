package com.aura.details.springdata.models.links;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.lessons.LessonHomeworkCompletionModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonParticipationReviewModel;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.lessons.LessonQuizStudentLessonLinkModel;
import com.aura.details.springdata.models.lessons.LessonTopicProficiencyModel;
import com.aura.details.springdata.models.lessons.StudentLessonAttendanceModel;
import com.aura.details.springdata.models.students.StudentModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "StudentLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_lesson",
                        columnNames = {"student_id", "lesson_id"}
                )
        })
public class StudentLessonLinkModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id", nullable = false)
    private StudentModel student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonModel lesson;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonPlanModel lessonPlan;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonHomeworkCompletionModel lessonHomeworkCompletion;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private StudentLessonAttendanceModel studentLessonAttendance;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonParticipationReviewModel lessonParticipationReview;

    @OneToMany(mappedBy = "studentLessonLink", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonQuizStudentLessonLinkModel> lessonQuizStudentLessonLinks = new HashSet<>();

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonTopicProficiencyModel lessonTopicProficiency;

}
