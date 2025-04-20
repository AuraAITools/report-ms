package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.LessonPlanMaterialsAttachment;
import com.reportai.www.reportapi.entities.attachments.MaterialLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
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
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Materials")
public class Material extends TenantAwareBaseEntity {

    private String name;

    private String fileUrl;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MaterialTopicAttachment> materialTopicAttachments = new HashSet<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MaterialLessonAttachment> materialLessonAttachments = new HashSet<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanMaterialsAttachment> lessonPlanMaterialsAttachments = new HashSet<>();
}
