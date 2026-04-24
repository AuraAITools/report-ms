package com.aura.details.springdata.models.outlets;


import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.OutletEducatorLinkModel;
import com.aura.details.springdata.models.links.StudentOutletLinkModel;
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
@Table(name = "Outlets", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_outlet_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class OutletModel extends TenantBaseModel {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    private String description;

    @Email
    private String email;

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<StudentOutletLinkModel> studentOutletLinks = new HashSet<>();


    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<OutletEducatorLinkModel> outletEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<CourseModel> courses = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<OutletRoomModel> outletRoom = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonModel> lessons = new HashSet<>();

}
