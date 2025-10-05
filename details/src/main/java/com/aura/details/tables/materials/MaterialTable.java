package com.aura.details.tables.materials;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.links.LessonPlanMaterialsLinkTable;
import com.aura.details.tables.links.MaterialLessonLinkTable;
import com.aura.details.tables.links.MaterialTopicLinkTable;
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
@Table(name = "Materials")
public class MaterialTable extends TenantBaseTable {

    private String name;

    private String fileUrl;

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MaterialTopicLinkTable> materialTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MaterialLessonLinkTable> materialLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanMaterialsLinkTable> lessonPlanMaterialsLinks = new HashSet<>();
}
