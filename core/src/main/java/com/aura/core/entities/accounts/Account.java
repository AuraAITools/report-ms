package com.aura.core.entities.accounts;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String id;

    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private String contact;
    private RELATIONSHIP relationship;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

}
