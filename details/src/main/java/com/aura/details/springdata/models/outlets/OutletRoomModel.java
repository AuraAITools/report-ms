package com.aura.details.springdata.models.outlets;

import com.aura.core.entities.outlets.OutletRoom.OUTLET_ROOM_TYPE;
import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "OutletRooms", uniqueConstraints = {
        @UniqueConstraint(name = "uk_tenant_outlet_name", columnNames = {"name", "tenant_id"})
})
public class OutletRoomModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private OutletModel outlet;

    private String name;

    private int size;

    private String details;

    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OUTLET_ROOM_TYPE type;

    private String url;

    private String passcode;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonOutletRoomBookingModel> lessonOutletRoomBookings = new HashSet<>();

}
