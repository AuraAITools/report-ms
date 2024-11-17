package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "StudentReports")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Lesson lesson;

    @OneToOne(fetch = FetchType.EAGER)
    private Student student;

}
