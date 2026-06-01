package com.aura.details.springdata.models.links;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.students.StudentModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * The student's registration to a lesson
 */
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
    private LessonGroupModel lessonGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private STATE state = STATE.REGISTERED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ATTENDANCE_STATE attendanceState = ATTENDANCE_STATE.UNFILLED;

    @OneToOne(mappedBy = "studentLessonLink", cascade = CascadeType.ALL)
    @ToString.Exclude
    private StudentLessonReportModel studentLessonReport;

    public enum STATE {
        REGISTERED,
        UNREGISTERED
    }

    public enum ATTENDANCE_STATE {
        UNFILLED,
        ATTENDED,
        NOT_ATTENDED
    }
}
