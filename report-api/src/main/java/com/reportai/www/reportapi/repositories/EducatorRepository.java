package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.educators.Educator;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducatorRepository extends JpaRepository<Educator, UUID> {

}
