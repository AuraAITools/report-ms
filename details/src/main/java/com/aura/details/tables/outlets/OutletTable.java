package com.aura.details.tables.outlets;


import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.courses.CourseTable;
import com.aura.details.tables.lessons.LessonTable;
import com.aura.details.tables.links.OutletEducatorLinkTable;
import com.aura.details.tables.links.StudentOutletLinkTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
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
@Table(name = "Outlets", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_outlet_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class OutletTable extends TenantBaseTable {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    private String description;

    @Email
    private String email;

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<StudentOutletLinkTable> studentOutletLinks = new HashSet<>();


    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OutletEducatorLinkTable> outletEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<CourseTable> courses = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<OutletRoomTable> outletRoomTables = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonTable> lessons = new HashSet<>();

}
