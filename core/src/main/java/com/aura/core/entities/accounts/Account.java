package com.aura.core.entities.accounts;

import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.AccountStudentLink;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    private Long version;

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
    private Set<AccountStudentLink> accountStudentLinks = new HashSet<>();
    private Set<AccountEducatorLink> accountEducatorLinks = new HashSet<>();

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

}
