package com.reportai.www.reportapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Courses")
public class Course extends BaseEntity {

    public enum LESSON_FREQUENCY {
        ONCE_PER_DAY,
        TWICE_PER_WEEK,
        THRICE_PER_WEEK,
        FOUR_TIMES_PER_WEEK,
        ONCE_PER_WEEK,
        ONCE_PER_TWO_WEEKS,
        ONCE_PER_MONTH,
        ONCE_PER_TWO_MONTHS,
        MANUAL_SELECT_LESSONS
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LESSON_FREQUENCY lessonFrequency;

    @OneToOne
    @PrimaryKeyJoinColumn
    private PriceRecord priceRecord;

    private String name;

    @Column(nullable = false)
    private Integer maxSize;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Outlet outlet;

    // Note: deleting a course will delete all subjects under the course
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;

    @ManyToOne(fetch = FetchType.LAZY)
    private Level level;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Student> students;

    @Column(nullable = false)
    private String tenantId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "educator_id")
    )
    private List<Educator> educators;
}
