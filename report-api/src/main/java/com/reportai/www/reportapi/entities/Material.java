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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Materials")
public class Material extends BaseEntity {

    private String name;

    private String fileUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")

    )
    private List<Topic> topics;

    @ManyToMany(mappedBy = "materials", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;
}
