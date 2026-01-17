package com.aura.details.springdata.tables.lessons;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.courses.CourseTable;
import com.aura.details.springdata.tables.links.EducatorLessonLinkTable;
import com.aura.details.springdata.tables.links.MaterialLessonLinkTable;
import com.aura.details.springdata.tables.links.StudentLessonLinkTable;
import com.aura.details.springdata.tables.outlets.OutletTable;
import com.aura.details.springdata.tables.subjects.SubjectTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
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
@Table(name = "Lessons", uniqueConstraints = {@UniqueConstraint(name = "uk_tenant_lesson_name", columnNames = {"tenant_id", "name"})})
public class LessonTable extends TenantBaseTable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant lessonStartTimestamptz;

    @Column(nullable = false)
    private Instant lessonEndTimestamptz;

    @Builder.Default
    private String description = "";

    @Builder.Default
    private String recap = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SubjectTable subject;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonPostponementRequestTable> lessonPostponementRequests = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonCancellationRequestTable> lessonCancellationRequests = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonQuizTable> lessonQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonPlanTable> lessonPlans = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonObjectiveTable> lessonObjectives = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentLessonLinkTable> studentLessonRegistrations = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<EducatorLessonLinkTable> educatorLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<MaterialLessonLinkTable> materialLessonLinks = new HashSet<>();

    @OneToOne(mappedBy = "lesson")
    @ToString.Exclude
    private LessonOutletRoomBookingTable lessonOutletRoomBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourseTable course;

    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private OutletTable outlet;


    public enum LESSON_STATUS {
        COMPLETED, CANCELLED, POSTPONED, UPCOMING, ONGOING
    }

    public enum LESSON_REVIEW_STATUS {
        REVIEWED, NOT_REVIEWED,
    }

}
