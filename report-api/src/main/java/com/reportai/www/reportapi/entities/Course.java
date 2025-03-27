package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Courses")
public class Course extends BaseEntity {

    @Column(nullable = false)
    private Integer lessonNumberFrequency;

    @Column(nullable = false)
    private Integer lessonWeeklyFrequency;

    // TODO: make embeddable entity?
    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Institution institution;

    public Course addInstitution(Institution institution) {
        assert institution != null;
        assert this.getInstitution() != null : "course is attached to existing institution";
        this.setInstitution(institution);
        institution.getCourses().add(this);
        return this;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Outlet outlet;

    public Course addOutlet(Outlet outlet) {
        assert outlet != null;
        outlet.getCourses().add(this);
        this.setOutlet(outlet);
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Subject> subjects = new ArrayList<>();

    public Course addSubjects(Collection<Subject> subjects) {
        assert subjects != null;
        assert !subjects.isEmpty();

        this.getSubjects().addAll(subjects);
        subjects.forEach(subject -> subject.getCourses().add(this));
        return this;
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Level level;

    public Course addLevel(Level level) {
        assert level != null;
        assert this.getLevel() == null;

        this.setLevel(level);
        return this;
    }

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();

    public Course addStudent(Student student) {
        assert student != null;
        assert !this.getStudents().contains(student) : "incoming student is already registered in course";

        this.getStudents().add(student);
        student.getCourses().add(this);
        return this;
    }

    public Course addStudents(Collection<Student> students) {
        assert students != null;
        assert !students.isEmpty();
        assert Collections.disjoint(this.getStudents(), students) : "incoming student is already registered in course";

        this.getStudents().addAll(students);
        students.forEach(student -> student.getCourses().add(this));
        return this;
    }

    @Column(nullable = false)
    private String tenantId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "educator_id")
    )
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ToString.Exclude
    private List<Educator> educators = new ArrayList<>();

    public Course addEducators(Collection<Educator> educators) {
        assert educators != null;
        assert !educators.isEmpty();
        assert Collections.disjoint(this.getEducators(), educators) : "incoming educator is already registered in course";

        this.getEducators().addAll(educators);
        educators.forEach(educator -> educator.getCourses().add(this));
        return this;
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();

    public Course addLessons(List<Lesson> lessons) {
        assert lessons != null;
        assert !lessons.isEmpty();
        assert Collections.disjoint(lessons, this.getLessons());

        this.getLessons().addAll(lessons);
        lessons.forEach(lesson -> lesson.setCourse(this));
        return this;
    }

    /**
     * TODO: remove?
     */
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ToString.Exclude
    private List<LessonGenerationTemplate> lessonGenerationTemplates = new ArrayList<>();
}
