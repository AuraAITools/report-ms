package com.aura.app.controllers.graphql.v1.parents;

import com.aura.app.codegen.types.DeleteParentByIdInput;
import com.aura.app.codegen.types.GetParentByIdInput;
import com.aura.app.codegen.types.ParentResponse;
import com.aura.app.codegen.types.UpdateParentByIdInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.ParentMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Parent;
import com.aura.details.services.ParentService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ParentsGqlController {

    private final ParentService parentService;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<ParentResponse> getAllParentsInInstitution(@Argument("institutionId") String institutionId,
                                                           @ContextValue("user") User user) {
        return parentService.findAllByTenantId(institutionId).stream()
                .map(ParentMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'view')")
    @QueryMapping
    public ParentResponse getParentById(@Argument("input") GetParentByIdInput input,
                                        @ContextValue("user") User user) {
        return Optional.ofNullable(parentService.findById(input.getId()))
                .map(ParentMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Parent"));
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public ParentResponse updateParentById(@Argument("input") UpdateParentByIdInput input,
                                           @ContextValue("user") User user) {
        Parent.RELATIONSHIP rel = input.getRelationship() == null
                ? null
                : Parent.RELATIONSHIP.valueOf(input.getRelationship().name());
        Parent updated = parentService.updateById(input.getId(), rel);
        return ParentMapper.INSTANCE.toDto(updated);
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'delete')")
    @MutationMapping
    public Boolean deleteParentById(@Argument("input") DeleteParentByIdInput input,
                                    @ContextValue("user") User user) {
        parentService.deleteById(input.getId());
        return true;
    }
}
