package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Topics")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Topic extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @ManyToMany(mappedBy = "topics", fetch = FetchType.LAZY)
    private Set<Material> materials;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Objective> objectives;

}
