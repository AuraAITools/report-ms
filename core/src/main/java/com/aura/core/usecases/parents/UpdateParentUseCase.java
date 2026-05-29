package com.aura.core.usecases.parents;

import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.accounts.Parent.RELATIONSHIP;
import org.jspecify.annotations.Nullable;

public interface UpdateParentUseCase {
    Parent update(String id, @Nullable RELATIONSHIP relationship);
}
