package com.aura.core.clients;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserIdentity {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailVerified;
    private Boolean enabled;
    private Map<String, List<String>> attributes;
    private Long createdTimestamp;
    private List<IdentityCredential> credentials;
    private List<String> requiredActions;
    private Map<String, List<String>> roles; // key is tenant-id, value is list of roles
}
