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
public class InstitutionCreationRequest {
    private String id;
    private String institutionName;
    private String institutionEmail;
    private String institutionContactNumber;
    private String institutionAddress;
    private String institutionUen;
    private String requesterFirstName;
    private String requesterLastName;
    private String requesterEmail;
    private String requesterContactNumber;
    private STATE state;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    public enum STATE {
        REQUESTED,
        FULFILLED,
        REJECTED
    }
}
