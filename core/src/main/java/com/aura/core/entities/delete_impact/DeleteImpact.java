package com.aura.core.entities.delete_impact;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteImpact {
    private String resourceType;
    private String resourceId;
    private List<AffectedResourceGroup> affectedGroups;
}
