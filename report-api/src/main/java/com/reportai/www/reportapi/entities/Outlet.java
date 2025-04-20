package com.reportai.www.reportapi.entities;


import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.helpers.EntityRelationshipUtils;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import com.reportai.www.reportapi.entities.views.LessonView;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.modelmapper.internal.util.Assert;

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
public class Outlet extends TenantAwareBaseEntity {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    private String description;

    @Email
    private String email;


    @ManyToMany(mappedBy = "outlets", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<OutletAdminPersona> outletAdminPersonas = new HashSet<>();

    public Outlet addOutletAdminPersona(@NonNull OutletAdminPersona outletAdminPersona) {
        return EntityRelationshipUtils.addToManyToMany(
                this, outletAdminPersona, this.getOutletAdminPersonas(),
                outletAdminPersona.getOutlets()
        );
    }

    public Outlet addOutletAdminPersonas(@NonNull Collection<OutletAdminPersona> outletAdminPersonas) {
        Assert.isTrue(Collections.disjoint(this.getOutletAdminPersonas(), outletAdminPersonas));
        Assert.isTrue(!outletAdminPersonas.isEmpty());

        return EntityRelationshipUtils.addAllToManyToMany(
                this, outletAdminPersonas, this.getOutletAdminPersonas(),
                OutletAdminPersona::getOutlets
        );
    }

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<StudentOutletRegistration> studentOutletRegistrations = new HashSet<>();


    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OutletEducatorAttachment> outletEducatorAttachments = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<OutletRoom> outletRooms = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "outlet", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonView> lessonViews = new HashSet<>();

}
