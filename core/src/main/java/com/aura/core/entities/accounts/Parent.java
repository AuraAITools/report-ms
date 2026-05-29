package com.aura.core.entities.accounts;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parent {

    private String id;
    private Long version;
    private RELATIONSHIP relationship;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;

    private @Nullable String accountId;

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }
}
