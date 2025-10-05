package com.aura.core.entities.lessons;

import com.aura.core.entities.outlets.OutletRoom;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LessonOutletRoomBooking {

    private UUID id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private OutletRoom outletRoom;

    private Lesson lesson;

    private Instant startTimestampz;

    private Instant endTimestampz;
}