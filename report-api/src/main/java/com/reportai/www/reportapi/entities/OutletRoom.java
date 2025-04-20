package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.helpers.EntityRelationshipUtils;
import com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OutletRooms", uniqueConstraints = {
        @UniqueConstraint(name = "uk_tenant_outlet_name", columnNames = {"name", "tenant_id"})
})
public class OutletRoom extends TenantAwareBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Outlet outlet;

    public OutletRoom addOutlet(Outlet outlet) {
        return EntityRelationshipUtils.addToManyToOne(this, outlet, this::setOutlet, outlet.getOutletRooms());
    }

    private String name;

    private int size;

    private String details;

    private String fileUrl;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonOutletRoomBooking> lessonOutletRoomBookings = new HashSet<>();

}
