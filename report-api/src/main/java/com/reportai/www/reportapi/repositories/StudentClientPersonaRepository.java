package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentClientPersonaRepository extends JpaRepository<StudentClientPersona, UUID>, JpaSpecificationExecutor<StudentClientPersona> {
}
