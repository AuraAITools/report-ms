package com.reportai.www.reportapi.entities.attachments;


import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import lombok.AllArgsConstructor;
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
@Table(name = "EducatorLessonAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_educator_lesson",
                        columnNames = {"educator_id", "lesson_id"}
                )
        })
public class EducatorLessonAttachment extends AttachmentTenantAwareBaseEntityTemplate<Educator, Lesson> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private Educator educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Override
    public Educator getFirstEntity() {
        return educator;
    }

    @Override
    public Lesson getSecondEntity() {
        return lesson;
    }

    @Override
    public Collection<EducatorLessonAttachment> getFirstEntityAttachments() {
        return educator != null ? educator.getEducatorLessonAttachments() : null;
    }

    @Override
    public Collection<EducatorLessonAttachment> getSecondEntityAttachments() {
        return lesson != null ? lesson.getEducatorLessonAttachments() : null;
    }

    @Override
    public EducatorLessonAttachment create(Educator entity1, Lesson entity2) {
        return EducatorLessonAttachment
                .builder()
                .educator(entity1)
                .lesson(entity2)
                .build();
    }
}
