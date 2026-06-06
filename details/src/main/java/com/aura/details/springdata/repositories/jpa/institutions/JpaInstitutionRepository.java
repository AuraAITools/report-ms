package com.aura.details.springdata.repositories.jpa.institutions;

import com.aura.details.springdata.models.institutions.InstitutionModel;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstitutionRepository extends JpaRepository<InstitutionModel, UUID>, JpaSpecificationExecutor<InstitutionModel> {

    @Modifying
    @Query("update InstitutionModel i set i.profileImageKey = :key where i.id = :id")
    int updateProfileImageKeyById(@Param("id") UUID id, @Param("key") @Nullable String key);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}

