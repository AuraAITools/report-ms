package com.aura.core.entities.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TenantAwareRole extends Role {
    private ROLE_TYPE roleType;
    private String outletId;
    private String institutionId;
    private String tenantId;

    public enum ROLE_TYPE {
        INSTITUTION_LEVEL,
        OUTLET_LEVEL,
        TECHNICAL_USER
    }
}
