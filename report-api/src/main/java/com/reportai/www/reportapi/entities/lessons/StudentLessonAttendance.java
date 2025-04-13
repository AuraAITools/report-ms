package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentLessonAttendances")
public class StudentLessonAttendance extends TenantAwareBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private StudentLessonRegistration studentLessonRegistration;


    private boolean attended;
}
