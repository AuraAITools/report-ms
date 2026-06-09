package com.aura.app.mappers;

import com.aura.app.codegen.types.AffectedResourceGroup;
import com.aura.app.codegen.types.CleanupAction;
import com.aura.app.codegen.types.DeleteImpactPreview;
import com.aura.core.entities.delete_impact.DeleteImpact;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeleteImpactMapper {

    DeleteImpactMapper INSTANCE = Mappers.getMapper(DeleteImpactMapper.class);

    default DeleteImpactPreview toDto(DeleteImpact impact) {
        if (impact == null) {
            return null;
        }
        List<AffectedResourceGroup> groups = impact.getAffectedGroups() == null
                ? List.of()
                : impact.getAffectedGroups().stream().map(this::toGroupDto).toList();
        int total = groups.stream().mapToInt(AffectedResourceGroup::getCount).sum();
        return DeleteImpactPreview.newBuilder()
                .resourceType(impact.getResourceType())
                .resourceId(impact.getResourceId())
                .affectedGroups(groups)
                .totalAffectedCount(total)
                .build();
    }

    default AffectedResourceGroup toGroupDto(com.aura.core.entities.delete_impact.AffectedResourceGroup group) {
        List<String> ids = group.getAffectedIds() == null ? List.of() : group.getAffectedIds();
        return AffectedResourceGroup.newBuilder()
                .resourceType(group.getResourceType())
                .cleanupAction(toCleanupActionDto(group.getCleanupAction()))
                .count(ids.size())
                .affectedIds(ids)
                .build();
    }

    default CleanupAction toCleanupActionDto(com.aura.core.entities.delete_impact.CleanupAction action) {
        return switch (action) {
            case DELETE -> CleanupAction.DELETE;
            case NULL_OUT -> CleanupAction.NULL_OUT;
        };
    }
}
