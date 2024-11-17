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
@Table(name = "Materials")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material extends BaseEntity {

    private String name;

    private String fileUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Topic> topics;

    @ManyToMany(mappedBy = "materials", fetch = FetchType.LAZY)
    private Set<Lesson> lessons;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

}
