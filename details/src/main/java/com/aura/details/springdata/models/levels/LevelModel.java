package com.aura.details.springdata.models.levels;

import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.LevelEducatorLinkModel;
import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "Levels", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_tenant_level_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class LevelModel extends TenantBaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchoolCategory category;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectLevelLinkModel> subjectLevelLinks = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LevelEducatorLinkModel> levelEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<CourseModel> courses = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<StudentModel> students = new HashSet<>();


}
