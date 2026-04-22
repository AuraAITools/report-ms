package com.aura.core.entities.institutions;

import com.aura.core.entities.outlets.Outlet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Institution {
    private String id;
    private String name;
    private String email;
    private String uen;
    private String address;
    private String contactNumber;
    @Builder.Default
    private List<Outlet> outlets = new ArrayList<>();
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
}
