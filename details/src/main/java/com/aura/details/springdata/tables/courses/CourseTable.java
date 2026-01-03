package com.aura.details.springdata.tables.courses;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.lessons.LessonTable;
import com.aura.details.springdata.tables.lessons.PriceRecordTable;
import com.aura.details.springdata.tables.levels.LevelTable;
import com.aura.details.springdata.tables.links.EducatorCourseLinkTable;
import com.aura.details.springdata.tables.links.StudentCourseLinkTable;
import com.aura.details.springdata.tables.links.SubjectCourseLinkTable;
import com.aura.details.springdata.tables.outlets.OutletTable;
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
@Table(name = "Courses")
public class CourseTable extends TenantBaseTable {

    @Enumerated(EnumType.STRING)
    @Column
    private LESSON_FREQUENCY lessonFrequency;

    // TODO: make embeddable entity?
    @OneToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private PriceRecordTable priceRecord;

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
    private OutletTable outlet;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<SubjectCourseLinkTable> subjectCourseLinks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LevelTable level;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default

    private Set<StudentCourseLinkTable> studentCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<EducatorCourseLinkTable> educatorCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonTable> lessons = new HashSet<>();


    public enum LESSON_FREQUENCY {
        WEEKLY,
        FORTNIGHTLY,
        MONTHLY
    }

}
