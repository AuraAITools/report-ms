package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.LessonObjective;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.MaterialLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.helpers.EntityRelationshipUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Lessons")
public class Lesson extends TenantAwareBaseEntity {

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

    @Column(nullable = false)
    private String name;

    private LocalDate date;

    @Column(nullable = false)
    private DayOfWeek day;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Builder.Default
    private String description = "";

    @Builder.Default
    private String recap = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Subject subject;

    public Lesson addSubject(Subject subject) {
        return EntityRelationshipUtils
                .addToManyToOne(
                        this,
                        subject,
                        this::setSubject,
                        subject.getLessons()
                );
    }

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonPostponementRequest> lessonPostponementRequests = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonPlan> lessonPlans = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonObjective> lessonObjectives = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentLessonRegistration> studentLessonRegistrations = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<EducatorLessonAttachment> educatorLessonAttachments = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MaterialLessonAttachment> materialLessonAttachments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Course course;

    public Lesson addCourse(Course course) {
        return EntityRelationshipUtils
                .addToManyToOne(
                        this,
                        course,
                        this::setCourse,
                        course.getLessons()
                );
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Outlet outlet;

    public Lesson addOutlet(Outlet outlet) {
        return EntityRelationshipUtils
                .addToManyToOne(
                        this,
                        outlet,
                        this::setOutlet,
                        outlet.getLessons()
                );
    }


}
