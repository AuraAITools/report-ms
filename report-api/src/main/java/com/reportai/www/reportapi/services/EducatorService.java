package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.services.commons.CRUDServiceSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EducatorService extends CRUDServiceSupport<Educator, UUID> {
    public EducatorService(JpaRepository<Educator, UUID> repository) {
        super(repository);
    }
}
