package com.aura.details.tables.lessons;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.outlets.OutletRoomTable;
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
@Table(name = "LessonOutletRoomBookings", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_outlet_room",
                columnNames = {"lesson_id", "outlet_room_id"}
        )
})
public class LessonOutletRoomBookingTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "outlet_room_id")
    private OutletRoomTable outletRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private LessonTable lesson;

    @Column(nullable = false)
    private Instant startTimestampz;

    @Column(nullable = false)
    private Instant endTimestampz;
}