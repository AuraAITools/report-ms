package com.reportai.www.reportapi.services.subjects;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SubjectsService {

    private final SubjectRepository subjectRepository;

    private final InstitutionRepository institutionRepository;

    public SubjectsService(SubjectRepository subjectRepository, InstitutionRepository institutionRepository) {
        this.subjectRepository = subjectRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public Subject createSubjectForInstitution(UUID id, Subject newSubject) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        institution.getSubjects().forEach(s -> {
            if (newSubject.getName().equals(s.getName())) {
                throw new ResourceAlreadyExistsException("Subject already exists");
            }
        });
        newSubject.setInstitution(institution);
        return subjectRepository.save(newSubject);
    }

    @Transactional
    public List<Subject> getAllSubjectsForInstitution(UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        return institution.getSubjects();
    }
}
