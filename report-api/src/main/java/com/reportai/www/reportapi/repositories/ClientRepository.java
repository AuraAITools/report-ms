package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.personas.Persona;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<Persona, UUID>, JpaSpecificationExecutor<Persona> {
}
