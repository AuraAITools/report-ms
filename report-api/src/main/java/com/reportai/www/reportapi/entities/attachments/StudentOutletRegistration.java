package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import lombok.AllArgsConstructor;
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
@Table(name = "StudentOutletRegistrations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_outlet",
                        columnNames = {"student_id", "outlet_id"}
                )
        })
public class StudentOutletRegistration extends AttachmentTenantAwareBaseEntityTemplate<Student, Outlet, StudentOutletRegistration> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @Override
    public Student getFirstEntity() {
        return student;
    }

    @Override
    public Outlet getSecondEntity() {
        return outlet;
    }

    @Override
    public Collection<StudentOutletRegistration> getFirstEntityAttachments() {
        return student != null ? student.getStudentOutletRegistrations() : null;
    }

    @Override
    public Collection<StudentOutletRegistration> getSecondEntityAttachments() {
        return outlet != null ? outlet.getStudentOutletRegistrations() : null;
    }

    @Override
    public StudentOutletRegistration create(Student entity1, Outlet entity2) {
        return StudentOutletRegistration
                .builder()
                .student(entity1)
                .outlet(entity2)
                .build();
    }
}
