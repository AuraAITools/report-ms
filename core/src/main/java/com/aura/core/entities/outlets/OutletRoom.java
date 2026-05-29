package com.aura.core.entities.outlets;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutletRoom {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String outletId;
    private String name;
    private int size;
    private String details;
    private String fileUrl;
    private OUTLET_ROOM_TYPE type;
    private String url;
    private String passcode;
    private String description;

    public enum OUTLET_ROOM_TYPE {
        PHYSICAL,
        VIRTUAL
    }
}
