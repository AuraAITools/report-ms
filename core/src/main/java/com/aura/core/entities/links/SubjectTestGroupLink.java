package com.aura.core.entities.links;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.tests.TestGroup;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectTestGroupLink {
    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Subject subject;

    private TestGroup testGroup;

}
