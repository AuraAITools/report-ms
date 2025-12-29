package com.aura.details.springdata.tables.links;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.lessons.LessonHomeworkCompletionTable;
import com.aura.details.springdata.tables.lessons.LessonParticipationReviewTable;
import com.aura.details.springdata.tables.lessons.LessonPlanTable;
import com.aura.details.springdata.tables.lessons.LessonQuizStudentLessonLinkTable;
import com.aura.details.springdata.tables.lessons.LessonTable;
import com.aura.details.springdata.tables.lessons.LessonTopicProficiencyTable;
import com.aura.details.springdata.tables.lessons.StudentLessonAttendanceTable;
import com.aura.details.springdata.tables.students.StudentTable;
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
@Table(name = "StudentLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_lesson",
                        columnNames = {"student_id", "lesson_id"}
                )
        })
public class StudentLessonLinkTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id", nullable = false)
    private StudentTable student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonTable lesson;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonPlanTable lessonPlan;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonHomeworkCompletionTable lessonHomeworkCompletion;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private StudentLessonAttendanceTable studentLessonAttendance;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonParticipationReviewTable lessonParticipationReview;

    @OneToMany(mappedBy = "studentLessonLink", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonQuizStudentLessonLinkTable> lessonQuizStudentLessonLinks = new HashSet<>();

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonTopicProficiencyTable lessonTopicProficiency;

}
