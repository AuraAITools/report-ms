package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.ParentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParentService {
    private final ParentRepository parentRepository;

    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    private Parent findById(UUID id){
        Optional<Parent> foundParent = parentRepository.findById(id);
        if (foundParent.isEmpty()){
            throw new NotFoundException("Parent not found");
        }
        return foundParent.get();
    }

    private List<Parent> findAllParents() {
        return parentRepository.findAll();
    }
}
