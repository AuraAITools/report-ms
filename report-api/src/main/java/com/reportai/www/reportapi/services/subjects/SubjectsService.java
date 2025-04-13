package com.reportai.www.reportapi.services.subjects;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SubjectsService implements BaseServiceTemplate<Subject, UUID> {

    private final SubjectRepository subjectRepository;

    private final InstitutionsService institutionsService;

    @Autowired
    public SubjectsService(SubjectRepository subjectRepository, InstitutionsService institutionsService) {
        this.subjectRepository = subjectRepository;
        this.institutionsService = institutionsService;
    }

    @Override
    public JpaRepository<Subject, UUID> getRepository() {
        return subjectRepository;
    }

    @Transactional
    public Subject createSubjectForInstitution(@NonNull Subject newSubject) {
        return subjectRepository.save(newSubject);
    }

    @Transactional
    public Subject updateSubjectForInstitution(@NonNull UUID subjectId, @NonNull Subject updatedSubject) {
        Subject subject = findById(subjectId);
        subject.setName(updatedSubject.getName());
        return subjectRepository.save(subject);
    }

    @Transactional
    public Collection<Subject> getAllSubjectsForInstitution(@NonNull UUID id) {
        return subjectRepository.findAll();
    }
}
