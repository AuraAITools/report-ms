package com.aura.core.entities.outlets;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
public class OutletRoom {

    private UUID id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Outlet outlet;
    private String name;
    private int size;
    private String details;
    private String fileUrl;
    private Set<LessonOutletRoomBooking> lessonOutletRoomBookings = new HashSet<>();

}
