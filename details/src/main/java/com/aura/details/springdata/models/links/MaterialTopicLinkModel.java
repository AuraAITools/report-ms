package com.aura.details.springdata.models.links;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.models.topics.TopicModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
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
@Table(name = "MaterialTopicLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_material_topic",
                        columnNames = {"material_id", "topic_id"}
                )
        })
public class MaterialTopicLinkModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private TopicModel topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "material_id")
    private MaterialModel material;

}
