package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.Topic;
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
@Table(name = "MaterialTopicAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_material_topic",
                        columnNames = {"material_id", "topic_id"}
                )
        })
public class MaterialTopicAttachment extends AttachmentTenantAwareBaseEntityTemplate<Material, Topic, MaterialTopicAttachment> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "material_id")
    private Material material;

    @Override
    public Material getFirstEntity() {
        return material;
    }

    @Override
    public Topic getSecondEntity() {
        return topic;
    }

    @Override
    public Collection<MaterialTopicAttachment> getFirstEntityAttachments() {
        return material != null ? material.getMaterialTopicAttachments() : null;
    }

    @Override
    public Collection<MaterialTopicAttachment> getSecondEntityAttachments() {
        return topic != null ? topic.getMaterialTopicAttachments() : null;
    }

    @Override
    public MaterialTopicAttachment create(Material entity1, Topic entity2) {
        return MaterialTopicAttachment
                .builder()
                .material(entity1)
                .topic(entity2)
                .build();
    }
}
