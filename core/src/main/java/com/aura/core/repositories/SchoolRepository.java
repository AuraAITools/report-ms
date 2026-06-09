package com.aura.core.repositories;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.schools.School;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface SchoolRepository {
    @Nullable School findByNameAndTenantId(String name, String tenantId);

    List<School> findAllByTenantId(String tenantId);

    @Nullable School findById(String id);

    School save(School school);

    void deleteById(String id);

    DeleteImpact findDeleteImpact(String schoolId);

    void nullOutSchoolOnStudentsBySchoolId(String schoolId);
}
