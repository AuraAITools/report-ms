package com.aura.details.tables.levels;

import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.courses.CourseTable;
import com.aura.details.tables.links.LevelEducatorLinkTable;
import com.aura.details.tables.links.SubjectLevelLinkTable;
import com.aura.details.tables.students.StudentTable;
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
@Table(name = "Levels", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_tenant_level_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class LevelTable extends TenantBaseTable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchoolCategory category;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectLevelLinkTable> subjectLevelLinks = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LevelEducatorLinkTable> levelEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<CourseTable> courses = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<StudentTable> students = new HashSet<>();


}
