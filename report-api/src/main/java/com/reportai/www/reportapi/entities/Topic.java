package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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
    @JoinTable(
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "objective_id")
    )
    private List<Objective> objectives;

    @Column(nullable = false)
    private String tenantId;
}
