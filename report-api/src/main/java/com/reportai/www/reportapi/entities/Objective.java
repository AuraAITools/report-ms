package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "Objectives")
public class Objective extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;

    @ManyToMany(mappedBy = "objectives", fetch = FetchType.LAZY)
    private List<Topic> topics;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;
}
