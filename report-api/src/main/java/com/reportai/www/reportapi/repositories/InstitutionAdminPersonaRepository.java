package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.personas.InstitutionAdminPersona;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InstitutionAdminPersonaRepository extends JpaRepository<InstitutionAdminPersona, UUID>, JpaSpecificationExecutor<InstitutionAdminPersona> {
}
