package com.reportai.www.reportapi.entities.courses;

import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentCourseRegistration;
import com.reportai.www.reportapi.entities.attachments.SubjectCourseAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.helpers.EntityRelationshipUtils;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.modelmapper.internal.util.Assert;


import static java.util.Collections.disjoint;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Courses")
public class Course extends TenantAwareBaseEntity {

    @Column(nullable = false)
    private Integer lessonNumberFrequency;

    @Column(nullable = false)
    private Integer lessonWeeklyFrequency;

    // TODO: make embeddable entity?
    @OneToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private PriceRecord priceRecord;

    public Course addPriceRecord(PriceRecord priceRecord) {
        return EntityRelationshipUtils
                .setOneToOne(
                        this, priceRecord,
                        this::setPriceRecord,
                        priceRecord::setCourse);
    }

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
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Outlet outlet;

    public Course addOutlet(Outlet outlet) {
        return EntityRelationshipUtils.addToManyToOne(
                this, outlet,
                this::setOutlet,
                outlet.getCourses()
        );
    }

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<SubjectCourseAttachment> subjectCourseAttachments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Level level;

    public Course addLevel(Level level) {
        return EntityRelationshipUtils.addToManyToOne(
                this, level,
                this::setLevel,
                level.getCourses()
        );
    }

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<StudentCourseRegistration> studentCourseRegistrations = new HashSet<>();


    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<EducatorCourseAttachment> educatorCourseAttachments = new HashSet<>();


    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @Builder.Default
    @ToString.Exclude
    private Set<Lesson> lessons = new HashSet<>();

    public Course addLessons(Collection<Lesson> lessons) {
        Assert.notNull(lessons);
        Assert.isTrue(!lessons.isEmpty());
        Assert.isTrue(disjoint(lessons, this.getLessons())); // incoming lessons must be different from existing lessons

        this.getLessons().addAll(lessons);
        lessons.forEach(lesson -> lesson.setCourse(this));
        return this;
    }

}
