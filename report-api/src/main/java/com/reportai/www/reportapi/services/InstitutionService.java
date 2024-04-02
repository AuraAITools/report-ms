package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.services.commons.CRUDServiceSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InstitutionService extends CRUDServiceSupport<Institution, UUID> {
    public InstitutionService(JpaRepository<Institution, UUID> repository) {
        super(repository);
    }
}
