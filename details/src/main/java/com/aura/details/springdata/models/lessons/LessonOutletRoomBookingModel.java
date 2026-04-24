package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AllArgsConstructor;
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
@Table(name = "LessonOutletRoomBookings", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_outlet_room",
                columnNames = {"lesson_id", "outlet_room_id"}
        )
})
public class LessonOutletRoomBookingModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "outlet_room_id")
    private OutletRoomModel outletRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private LessonModel lesson;

    @Column(nullable = false)
    private Instant startTimestampz;

    @Column(nullable = false)
    private Instant endTimestampz;
}
