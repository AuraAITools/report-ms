package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.User;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.ClassRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class RegistrationService {

    private final EducatorRepository educatorRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    private final InstitutionRepository institutionRepository;

    public RegistrationService(EducatorRepository educatorRepository, ParentRepository parentRepository, StudentRepository studentRepository, ClassRepository classRepository, SubjectRepository subjectRepository, UserRepository userRepository, InstitutionRepository institutionRepository) {
        this.educatorRepository = educatorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public Institution registerInstitution(Institution entity, User user) {
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        return institutionRepository.save(entity);
    }

    @Transactional
    public Educator registerEducatorWithInstitution(Educator entity, User user, UUID institutionId) {
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitutions(List.of(institution.get()));
        return educatorRepository.save(entity);
    }


    @Transactional
    public Parent registerParentWithInstitution(Parent entity, User user , UUID institutionId) {
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        entity.setInstitutions(List.of(institution.get()));
        return parentRepository.save(entity);
    }


    @Transactional
    public Student registerStudentWithInstitution(Student entity, User user, UUID institutionId) {
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitutions(List.of(institution.get()));
        return studentRepository.save(entity);
    }

    @Transactional
    public Class registerClassWithInstitution(Class entity, UUID institutionId) {
        Class newClass = classRepository.save(entity);
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitution(institution.get());
        return classRepository.save(entity);
    }

    @Transactional
    public Parent linkStudentToParent(UUID student_id, UUID parent_id) {
        Optional<Student> foundStudent = studentRepository.findById(student_id);
        Optional<Parent> foundParent = parentRepository.findById(parent_id);

        if (foundStudent.isEmpty() || foundParent.isEmpty()) {
            throw new NotFoundException("Student or Parent not found");
        }

        Student student = foundStudent.get();
        Parent parent = foundParent.get();

        student.getParents().add(parent);
        parent.getStudents().add(student);


        studentRepository.save(student);
        return parentRepository.save(parent);
    };

    @Transactional
    public Educator linkEducatorToClass(UUID educator_id, UUID class_id){
        Optional<Educator> foundEducator = educatorRepository.findById(educator_id);
        Optional<Class> foundClass = classRepository.findById(class_id);

        if (foundEducator.isEmpty() || foundClass.isEmpty()) {
            throw new NotFoundException("Educator or Class not found");
        }

        Educator educator = foundEducator.get();
        Class aClass = foundClass.get();

        educator.getClasses().add(aClass);
        aClass.getEducators().add(educator);

        classRepository.save(aClass);
        return educatorRepository.save(educator);
    };

    @Transactional
    public Student linkStudentToClass(UUID student_id, UUID class_id) {
        Optional<Student> foundStudent = studentRepository.findById(student_id);
        Optional<Class> foundClass = classRepository.findById(class_id);

        if (foundStudent.isEmpty() || foundClass.isEmpty()) {
            throw new NotFoundException("Student or Class not found");
        }

        Student student = foundStudent.get();
        Class aClass = foundClass.get();

        student.getClasses().add(aClass);
        aClass.getStudents().add(student);

        classRepository.save(aClass);
        return studentRepository.save(student);
    }

    @Transactional
    public Subject linkSubjectToClass(UUID subject_id, UUID class_id) {
        Optional<Subject> foundSubject = subjectRepository.findById(subject_id);
        Optional<Class> foundClass = classRepository.findById(class_id);

        if (foundSubject.isEmpty() || foundClass.isEmpty()) {
            throw new NotFoundException("Subject or Class not found");
        }

        Subject subject = foundSubject.get();
        Class aClass = foundClass.get();

        aClass.getSubjects().add(subject);

        classRepository.save(aClass);
        return subject;
    }
}
