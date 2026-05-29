package com.aura.core.usecases.parents;

import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.accounts.Parent.RELATIONSHIP;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.ParentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateParentUseCaseImpl implements UpdateParentUseCase {
    private final ParentRepository parentRepository;

    @Override
    public Parent update(String id, @Nullable RELATIONSHIP relationship) {
        Parent existing = Optional.ofNullable(parentRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Parent", id));
        if (relationship != null) existing.setRelationship(relationship);
        return parentRepository.save(existing);
    }
}
