package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "LessonQuizzes")
public class LessonQuizModel extends TenantBaseModel {

    @Column(nullable = true)
    private int score;

    @Column(nullable = false)
    @Builder.Default
    private int maxScore = 100;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonGroupPlanModel lessonGroupPlan;

    @OneToMany(mappedBy = "lessonQuizTable", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonQuizStudentLessonLinkModel> lessonQuizStudentLessonLinks = new HashSet<>();

}
