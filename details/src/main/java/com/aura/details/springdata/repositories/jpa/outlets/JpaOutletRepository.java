package com.aura.details.springdata.repositories.jpa.outlets;

import com.aura.details.springdata.models.outlets.OutletModel;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOutletRepository extends JpaRepository<OutletModel, UUID>, JpaSpecificationExecutor<OutletModel> {
    List<OutletModel> findByTenantIdIn(Collection<String> tenantIds);

    List<OutletModel> findByTenantId(String tenantId);
}

