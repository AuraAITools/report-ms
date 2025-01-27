package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "StudentReports")
public class StudentReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Lesson lesson;

    @OneToOne(fetch = FetchType.EAGER)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;
}
