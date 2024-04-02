package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.repositories.ClassRepository;
import com.reportai.www.reportapi.services.commons.CRUDServiceSupport;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClassService extends CRUDServiceSupport<Class, UUID> {
    public ClassService(ClassRepository repository) {
        super(repository);
    }
}
