package com.aura.core.entities.outlets;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
public class Outlet {
    private String id;

    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    private String description;

    private String email;

    private String tenantId;

    private Long version;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    private Set<OutletRoom> outletRoom = new HashSet<>();

}
