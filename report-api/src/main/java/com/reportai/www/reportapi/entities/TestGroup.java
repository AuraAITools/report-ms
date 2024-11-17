package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TestGroups")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestGroup extends BaseEntity {

    @OneToMany(mappedBy = "testGroup", fetch = FetchType.EAGER)
    private List<Test> tests;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> subjects;

}
