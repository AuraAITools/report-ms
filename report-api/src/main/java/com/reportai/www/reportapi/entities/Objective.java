package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Objectives")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Objective extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;

    @ManyToMany(mappedBy = "objectives", fetch = FetchType.LAZY)
    private List<Topic> topics;

}
