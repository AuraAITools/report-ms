package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutletAdminPersonaRepository extends JpaRepository<OutletAdminPersona, UUID>, JpaSpecificationExecutor<OutletAdminPersona> {
}
