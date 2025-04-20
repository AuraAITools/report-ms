package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.StudentCourseRegistration;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import com.reportai.www.reportapi.entities.attachments.SubjectStudentAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.helpers.EntityRelationshipUtils;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Students")
public class Student extends TenantAwareBaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private LocalDate dateOfBirth;

    private String currentSchool;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Level level;

    public Student addLevel(@NonNull Level level) {
        if (this.getLevel() != null) {
            throw new IllegalArgumentException("level already exists");
        }
        this.setLevel(level);
        level.getStudents().add(this);
        return this;
    }

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectStudentAttachment> subjectStudentAttachments = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestResult> testResults = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentLessonRegistration> studentLessonRegistrations = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private StudentClientPersona studentClientPersona;

    public Student addStudentClientPersona(@NonNull StudentClientPersona studentClientPersona) {
        return EntityRelationshipUtils.addToManyToOne(
                this,
                studentClientPersona,
                this::setStudentClientPersona,
                studentClientPersona.getStudents()
        );
    }

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentCourseRegistration> studentCourseRegistrations = new HashSet<>();


    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<StudentOutletRegistration> studentOutletRegistrations = new HashSet<>();


}
