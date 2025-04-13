package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.LessonObjective;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.MaterialLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.courses.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "lessons_view")
public class LessonView extends TenantAwareBaseEntity {

    public enum LESSON_STATUS {
        COMPLETED,
        CANCELLED,
        POSTPONED,
        UPCOMING,
        ONGOING
    }

    public enum LESSON_REVIEW_STATUS {
        REVIEWED,
        NOT_REVIEWED,
    }

    public enum LESSON_PLAN_STATUS {
        PLANNED,
        NOT_PLANNED
    }

    @Column(nullable = false)
    private String name;

    private LocalDate date;

    @Column(nullable = false)
    private DayOfWeek day;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String description;

    private String recap;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Subject subject;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @ToString.Exclude
    private Set<LessonPostponementRequest> lessonPostponementRequests = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @ToString.Exclude
    private Set<LessonPlan> lessonPlans = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @ToString.Exclude
    private Set<LessonObjective> lessonObjectives = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @ToString.Exclude
    private Set<StudentLessonRegistration> studentLessonRegistrations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @ToString.Exclude
    private Set<EducatorLessonAttachment> educatorLessonAttachments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Set<MaterialLessonAttachment> materialLessonAttachments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Course course;

    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Outlet outlet;

    // New calculated fields from the view
    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_status")
    private LESSON_STATUS lessonStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_review_status")
    private LESSON_REVIEW_STATUS lessonReviewStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_plan_status")
    private LESSON_PLAN_STATUS lessonPlanStatus;
}