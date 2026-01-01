package com.aura.core.clients;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IdentityCredential {
    String type;
    String value;
    boolean isTemporary;
}
