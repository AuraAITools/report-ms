package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
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
@Table(name = "LessonPlans")
public class LessonPlan extends TenantAwareBaseEntity {

    public enum LESSON_PLAN_STATUS {
        PLANNED,
        UNPLANNED
    }

    // TODO: decide to calculate or store field
    private LESSON_PLAN_STATUS lesson_plan_status;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Lesson lesson;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StudentLessonRegistration> registeredStudents;


}
