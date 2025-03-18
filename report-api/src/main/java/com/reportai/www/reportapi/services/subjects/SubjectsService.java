package com.reportai.www.reportapi.services.subjects;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import jakarta.transaction.Transactional;
import java.util.List;
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
    public Subject createSubjectForInstitution(@NonNull UUID id, @NonNull Subject newSubject) {
        Institution institution = institutionsService.findById(id);
        institution.getSubjects().forEach(s -> {
            if (newSubject.getName().equals(s.getName())) {
                throw new ResourceAlreadyExistsException("Subject already exists");
            }
        });
        /**
         * TODO: refactor in entity
         */
        newSubject.setInstitution(institution);
        return subjectRepository.save(newSubject);
    }

    @Transactional
    public Subject updateSubjectForInstitution(@NonNull UUID subjectId, @NonNull Subject updatedSubject) {
        Subject subject = findById(subjectId);
        subject.setName(updatedSubject.getName());
        return subjectRepository.save(subject);
    }

    @Transactional
    public List<Subject> getAllSubjectsForInstitution(@NonNull UUID id) {
        Institution institution = institutionsService.findById(id);
        return institution.getSubjects();
    }
}
