package com.aura.core.entities.institutions;

import java.time.Instant;
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
    private String logoKey;
    private String profileImageKey;
    private STATE state;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;

    public enum STATE {
        PENDING,
        ACTIVE,
        INACTIVE
    }
}
