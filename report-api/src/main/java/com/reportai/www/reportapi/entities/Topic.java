package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Topics")
public class Topic extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @ManyToMany(mappedBy = "topics", fetch = FetchType.LAZY)
    private List<Material> materials;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Objective> objectives;

    @Column(nullable = false)
    private String tenantId;
}
