package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.courses.Course;
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
public class Level extends TenantAwareBaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private School.SchoolCategory category;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectLevelAttachment> subjectLevelAttachments = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LevelEducatorAttachment> levelEducatorAttachments = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<Student> students = new HashSet<>();


}
