package com.aura.details.springdata.tables.outlets;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.lessons.LessonOutletRoomBookingTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
import org.hibernate.envers.Audited;

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
public class OutletRoomTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private OutletTable outlet;

    private String name;

    private int size;

    private String details;

    private String fileUrl;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonOutletRoomBookingTable> lessonOutletRoomBookings = new HashSet<>();

}
