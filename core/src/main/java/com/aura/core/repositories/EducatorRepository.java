package com.aura.core.repositories;

import com.aura.core.entities.educators.Educator;
import org.jspecify.annotations.Nullable;

public interface EducatorRepository {

    @Nullable Educator findById(String id);

    Educator save(Educator educator);
}
