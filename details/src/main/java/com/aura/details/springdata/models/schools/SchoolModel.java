package com.aura.details.springdata.models.schools;


import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.springdata.models.base.TenantBaseModel;
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
@Table(name = "Schools", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_school_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class SchoolModel extends TenantBaseModel {
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchoolCategory schoolCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    @ToString.Exclude
    @Builder.Default
    private Set<StudentModel> students = new HashSet<>();
}
