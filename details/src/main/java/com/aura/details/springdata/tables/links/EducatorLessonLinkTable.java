package com.aura.details.springdata.tables.links;


import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.educators.EducatorTable;
import com.aura.details.springdata.tables.lessons.LessonTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "EducatorLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_educator_lesson",
                        columnNames = {"educator_id", "lesson_id"}
                )
        })
public class EducatorLessonLinkTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private EducatorTable educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private LessonTable lesson;

}
