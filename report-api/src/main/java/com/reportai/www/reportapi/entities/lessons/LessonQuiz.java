package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonQuizzes")
public class LessonQuiz extends TenantAwareBaseEntity {

    @Column(nullable = true)
    private int score;

    @Column(nullable = false)
    @Builder.Default
    private int maxScore = 100;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Lesson lesson;

    @OneToMany(mappedBy = "lessonQuiz", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonQuizStudentLessonRegistrationAttachment> lessonQuizStudentLessonRegistrationAttachments = new HashSet<>();

}
