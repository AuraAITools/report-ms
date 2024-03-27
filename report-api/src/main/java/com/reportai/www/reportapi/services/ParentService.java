package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.ParentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParentService extends CRUDServiceSupport<Parent,UUID>{

    public ParentService(ParentRepository parentRepository) {
        super(parentRepository);
    }
}
