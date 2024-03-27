package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestService extends CRUDServiceSupport<Test, UUID> {
    public TestService(JpaRepository<Test, UUID> repository) {
        super(repository);
    }
}
