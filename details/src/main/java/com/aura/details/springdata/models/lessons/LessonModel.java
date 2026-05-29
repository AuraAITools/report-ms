package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import com.aura.details.springdata.models.links.LevelLessonLinkModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.links.SubjectLessonLinkModel;
import com.aura.details.springdata.models.links.TopicLessonLinkModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Lessons", uniqueConstraints = {@UniqueConstraint(name = "uk_tenant_lesson_name", columnNames = {"tenant_id", "name"})})
public class LessonModel extends TenantBaseModel {

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

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<SubjectLessonLinkModel> subjectLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<TopicLessonLinkModel> topicLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LevelLessonLinkModel> levelLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonPostponementRequestModel> lessonPostponementRequests = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonCancellationRequestModel> lessonCancellationRequests = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonQuizModel> lessonQuizzes = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonPlanModel> lessonPlans = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentLessonLinkModel> studentLessonRegistrations = new HashSet<>();

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<EducatorLessonLinkModel> educatorLessonLinks = new HashSet<>();

    @OneToOne(mappedBy = "lesson")
    @ToString.Exclude
    private LessonOutletRoomBookingModel lessonOutletRoomBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourseModel course;

    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private OutletModel outlet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LESSON_STATE state = LESSON_STATE.CREATED;


    public enum LESSON_STATE {
        CREATED, ENDED, CANCELLED
    }

    public enum LESSON_REVIEW_STATUS {
        REVIEWED, NOT_REVIEWED,
    }

}
