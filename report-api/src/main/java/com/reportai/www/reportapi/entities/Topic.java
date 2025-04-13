package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.LessonObjectiveTopicAttachment;
import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "Topics")
public class Topic extends TenantAwareBaseEntity {

    private String name;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MaterialTopicAttachment> materialTopicAttachments = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<LessonObjectiveTopicAttachment> lessonObjectiveTopicAttachments = new HashSet<>();

}
