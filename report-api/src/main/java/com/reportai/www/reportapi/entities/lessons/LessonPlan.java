package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.attachments.LessonPlanMaterialsAttachment;
import com.reportai.www.reportapi.entities.attachments.LessonPlanTopicAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "LessonPlans")
public class LessonPlan extends TenantAwareBaseEntity {

    public enum LESSON_PLAN_STATUS {
        PLANNED,
        UNPLANNED
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Lesson lesson;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isPlanned;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StudentLessonRegistration> registeredStudents;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanMaterialsAttachment> lessonPlanMaterialsAttachments = new HashSet<>();

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanTopicAttachment> lessonPlanTopicAttachments = new HashSet<>();

}
