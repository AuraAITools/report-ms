package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.LessonObjectiveTopicAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.lessons.Lesson;
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
@Table(name = "LessonObjectives")
public class LessonObjective extends TenantAwareBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Lesson lesson;

    @OneToMany(mappedBy = "lessonObjective", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<LessonObjectiveTopicAttachment> lessonObjectiveTopicAttachments = new HashSet<>();


}
