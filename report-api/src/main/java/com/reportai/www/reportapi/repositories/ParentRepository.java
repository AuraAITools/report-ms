package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {
}
