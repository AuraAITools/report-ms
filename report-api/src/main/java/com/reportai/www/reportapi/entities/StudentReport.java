package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentReports")
public class StudentReport extends TenantAwareBaseEntity {

    @OneToOne(mappedBy = "studentReport")
    private StudentLessonRegistration studentLessonRegistration;

}
