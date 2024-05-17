package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.ClassRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.repositories.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InstitutionAdminService {

    private final InstitutionRepository institutionRepository;
    private final ClassRepository classRepository;

    private final SubjectRepository subjectRepository;

    private final EducatorRepository educatorRepository;

    private final StudentRepository studentRepository;

    private final TopicRepository topicRepository;

    public InstitutionAdminService(InstitutionRepository institutionRepository, ClassRepository classRepository, SubjectRepository subjectRepository, EducatorRepository educatorRepository, StudentRepository studentRepository, TopicRepository topicRepository) {
        this.institutionRepository = institutionRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public Class createClassForInstitution(Class newClass, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()->new NotFoundException("no institution found"));
        Class savedClass = classRepository.save(newClass);
        institution.getClasses().add(savedClass);
        institutionRepository.save(institution);
        return savedClass;
    }

    @Transactional
    public List<Class> createClassesForInstitution(List<Class> classes, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()-> new NotFoundException("no institution found"));
        List<Class> savedClasses = classRepository.saveAll(classes);
        institution.getClasses().addAll(savedClasses);
        institutionRepository.save(institution);
        return savedClasses;
    }

    @Transactional
    public Topic createTopicForInstitution(Topic topic, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()-> new NotFoundException("institution does not exist"));
        Topic savedTopic = topicRepository.save(topic);
        institution.getTopics().add(savedTopic);
        institutionRepository.save(institution);
        return savedTopic;
    }


    @Transactional
    public List<Subject> createSubjectsForClass(List<Subject> subjects, UUID classId) {
        Class targetClass = classRepository.findById(classId).orElseThrow(()-> new NotFoundException("class not found"));
        List<Subject> savedSubjects = subjectRepository.saveAll(subjects);
        targetClass.getSubjects().addAll(savedSubjects);
        classRepository.save(targetClass);
        return savedSubjects;
    }

    @Transactional
    public List<Educator> addEducatorsToSubject(List<UUID> educatorIds, UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()-> new NotFoundException("Subject not found"));
        List<Educator> educators = educatorRepository.findAllById(educatorIds);
        subject.getEducators().addAll(educators);
        subjectRepository.save(subject);
        return educators;
    }

    @Transactional
    public List<Student> addStudentsToSubject(List<UUID> studentIds, UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()-> new NotFoundException("Subject not found"));
        List<Student> students = studentRepository.findAllById(studentIds);
        subject.getStudents().addAll(students);
        subjectRepository.save(subject);
        return students;
    }


}
