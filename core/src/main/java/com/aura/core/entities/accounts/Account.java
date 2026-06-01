package com.aura.core.entities.accounts;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.jspecify.annotations.Nullable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String id;
    private Long version;

    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private String contact;
    private STATE state;
    private AccountFeatureState parentFeatureState;
    private AccountFeatureState educatorFeatureState;
    private AccountFeatureState staffFeatureState;

    private @Nullable String profileImageKey;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;

    @With
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private @Nullable Parent parent;

    public enum STATE {
        VERIFYING,
        ACTIVE,
        RETIRED
    }
}
