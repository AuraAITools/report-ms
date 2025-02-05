package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.OutletAdmin;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutletAdminRepository extends JpaRepository<OutletAdmin, UUID>, JpaSpecificationExecutor<OutletAdmin> {
}
