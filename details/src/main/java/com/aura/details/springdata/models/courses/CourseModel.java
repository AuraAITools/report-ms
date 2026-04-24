package com.aura.details.springdata.models.courses;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.PriceRecordModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.EducatorCourseLinkModel;
import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
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
@Table(name = "Courses")
public class CourseModel extends TenantBaseModel {

    @Enumerated(EnumType.STRING)
    @Column
    private LESSON_FREQUENCY lessonFrequency;
    // TODO: make embeddable entity?
    @OneToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private PriceRecordModel priceRecord;
    private String name;
    @Column(nullable = false)
    private Integer maxSize;
    @Column(nullable = false)
    private Instant courseStartTimestamptz;
    @Column(nullable = false)
    private Instant courseEndTimestamptz;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private OutletModel outlet;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<SubjectCourseLinkModel> subjectCourseLinks = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LevelModel level;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default

    private Set<StudentCourseLinkModel> studentCourseLinks = new HashSet<>();
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<EducatorCourseLinkModel> educatorCourseLinks = new HashSet<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonModel> lessons = new HashSet<>();

    public enum LESSON_FREQUENCY {
        WEEKLY,
        FORTNIGHTLY,
        MONTHLY
    }

    // TODO: scheduling
    public class Schedule {
        private ScheduleType scheduleType;

        public enum ScheduleType {
            FIXED,
            RECURRING
        }

        
    }

}
