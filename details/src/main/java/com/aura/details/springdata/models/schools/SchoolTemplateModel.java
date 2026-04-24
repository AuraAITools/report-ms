package com.aura.details.springdata.models.schools;


import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.springdata.models.base.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "SchoolTemplates")
public class SchoolTemplateModel extends BaseModel {
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchoolCategory schoolCategory;
}
