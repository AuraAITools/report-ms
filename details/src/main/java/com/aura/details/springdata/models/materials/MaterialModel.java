package com.aura.details.springdata.models.materials;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import com.aura.details.springdata.models.links.MaterialLessonLinkModel;
import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "Materials")
public class MaterialModel extends TenantBaseModel {

    private String name;

    private String fileUrl;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<MaterialTopicLinkModel> materialTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<MaterialLessonLinkModel> materialLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanMaterialsLinkModel> lessonPlanMaterialsLinks = new HashSet<>();
}
