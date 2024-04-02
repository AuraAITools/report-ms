package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.services.commons.CRUDServiceSupport;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ParentService extends CRUDServiceSupport<Parent,UUID> {

    public ParentService(ParentRepository parentRepository) {
        super(parentRepository);
    }
}
