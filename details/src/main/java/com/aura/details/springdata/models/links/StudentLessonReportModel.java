package com.aura.details.springdata.models.links;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.lessons.LessonHomeworkCompletionModel;
import com.aura.details.springdata.models.lessons.LessonParticipationReviewModel;
import com.aura.details.springdata.models.lessons.LessonTopicProficiencyModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "StudentLessonReports")
public class StudentLessonReportModel extends TenantBaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_lesson_link_id", nullable = false, unique = true)
    private StudentLessonLinkModel studentLessonLink;

    @OneToOne(mappedBy = "studentLessonReport", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonHomeworkCompletionModel lessonHomeworkCompletion;

    @OneToOne(mappedBy = "studentLessonReport", cascade = CascadeType.ALL)
    @ToString.Exclude
    private LessonParticipationReviewModel lessonParticipationReview;

    @OneToMany(mappedBy = "studentLessonReport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonTopicProficiencyModel> lessonTopicProficiencies = new HashSet<>();
}
