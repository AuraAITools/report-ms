package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
}
