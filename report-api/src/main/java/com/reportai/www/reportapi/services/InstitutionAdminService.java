package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
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
    private final CourseRepository courseRepository;

    private final SubjectRepository subjectRepository;

    private final EducatorRepository educatorRepository;

    private final StudentRepository studentRepository;

    private final TopicRepository topicRepository;

    public InstitutionAdminService(InstitutionRepository institutionRepository, CourseRepository courseRepository, SubjectRepository subjectRepository, EducatorRepository educatorRepository, StudentRepository studentRepository, TopicRepository topicRepository) {
        this.institutionRepository = institutionRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.topicRepository = topicRepository;
    }

    // Courses
    public List<Course> getAllCoursesFromInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()->new NotFoundException("no institution found"));
        return institution.getCourses();
    }

    @Transactional
    public Course createCourseForInstitution(Course newCourse, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()->new NotFoundException("no institution found"));
        Course savedCourse = courseRepository.save(newCourse);
        institution.getCourses().add(savedCourse);
        institutionRepository.save(institution);
        return savedCourse;
    }

    @Transactional
    public List<Course> batchCreateCoursesForInstitution(List<Course> courses, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()-> new NotFoundException("no institution found"));
        List<Course> savedCourses = courseRepository.saveAll(courses);
        institution.getCourses().addAll(savedCourses);
        institutionRepository.save(institution);
        return savedCourses;
    }

    @Transactional
    public List<Subject> batchCreateSubjectsForCourse(List<Subject> subjects, UUID courseId) {
        Course targetCourse = courseRepository.findById(courseId).orElseThrow(()-> new NotFoundException("class not found"));
        List<Subject> savedSubjects = subjectRepository.saveAll(subjects);
        targetCourse.getSubjects().addAll(savedSubjects);
        courseRepository.save(targetCourse);
        return savedSubjects;
    }

    public List<Subject> getAllSubjectsFromCourse(UUID courseId) {
        Course targetCourse = courseRepository.findById(courseId).orElseThrow(()->new NotFoundException("class not found"));
        return targetCourse.getSubjects();
    }

    // Topics
    public List<Topic> getAllTopicsFromInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()->new NotFoundException("no institution found"));
        return institution.getTopics();
    }

    @Transactional
    public Topic createTopicForInstitution(Topic topic, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(()-> new NotFoundException("institution does not exist"));
        Topic savedTopic = topicRepository.save(topic);
        institution.getTopics().add(savedTopic);
        institutionRepository.save(institution);
        return savedTopic;
    }

    // Subjects
    public List<Educator> getAllEducatorsFromSubject(UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()->new NotFoundException("Subject not found"));
        return subject.getEducators();
    }

    public List<Student> getAllStudentsFromSubject(UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new NotFoundException("Subject not found"));
        return subject.getStudents().stream().toList();
    }

    @Transactional
    public List<Educator> batchAddEducatorsToSubject(List<UUID> educatorIds, UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()-> new NotFoundException("Subject not found"));
        List<Educator> educators = educatorRepository.findAllById(educatorIds);
        subject.getEducators().addAll(educators);
        subjectRepository.save(subject);
        return educators;
    }

    @Transactional
    public List<Student> batchAddStudentsToSubject(List<UUID> studentIds, UUID subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()-> new NotFoundException("Subject not found"));
        List<Student> students = studentRepository.findAllById(studentIds);
        subject.getStudents().addAll(students);
        subjectRepository.save(subject);
        return students;
    }

    @Transactional
    public Educator removeEducatorFromSubject(UUID subjectId, UUID educatorId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()-> new NotFoundException("Subject not found"));
        Educator educator = educatorRepository.findById(educatorId).orElseThrow(() -> new NotFoundException("Educator not found"));
        subject.getEducators().remove(educator);
        subjectRepository.save(subject);
        return educator;
    }

    @Transactional
    public Student removeStudentFromSubject(UUID subjectId, UUID studentId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()-> new NotFoundException("Subject not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));
        subject.getStudents().remove(student);
        subjectRepository.save(subject);
        return student;
    }
}
