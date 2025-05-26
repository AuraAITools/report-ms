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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
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
import org.hibernate.envers.Audited;
import org.modelmapper.internal.util.Assert;


import static java.util.Collections.disjoint;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Courses")
public class Course extends TenantAwareBaseEntity {

    public enum LESSON_FREQUENCY {
        WEEKLY,
        FORTNIGHTLY,
        MONTHLY
    }

    @Enumerated(EnumType.STRING)
    @Column
    private LESSON_FREQUENCY lessonFrequency;

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
    private Instant courseStartTimestamptz;

    @Column(nullable = false)
    private Instant courseEndTimestamptz;

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
