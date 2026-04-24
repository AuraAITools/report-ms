package com.aura.details.springdata.models.links;


import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
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
@Table(name = "EducatorLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_educator_lesson",
                        columnNames = {"educator_id", "lesson_id"}
                )
        })
public class EducatorLessonLinkModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private EducatorModel educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private LessonModel lesson;

}
