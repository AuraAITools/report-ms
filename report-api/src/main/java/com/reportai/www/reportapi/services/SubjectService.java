package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubjectService extends CRUDServiceSupport<Subject, UUID> {
    public SubjectService(JpaRepository<Subject, UUID> repository) {
        super(repository);
    }
}
