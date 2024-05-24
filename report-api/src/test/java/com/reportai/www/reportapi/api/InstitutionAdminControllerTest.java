package com.reportai.www.reportapi.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.repositories.TopicRepository;
import com.reportai.www.reportapi.services.InstitutionAdminService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstitutionAdminService institutionAdminService;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private EducatorRepository educatorRepository;

    @Autowired
    private StudentRepository studentRepository;

    Institution institution;
    Course course;
    Subject subject;
    Educator educator;
    Student student;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        institution = institutionRepository.save(Institution
                .builder()
                .name("name")
                .email("test@gmail.com")
                .userId(UUID.randomUUID())
                .build());
        course = courseRepository.save(Course.builder().build());
        subject = subjectRepository.save(Subject.builder().build());
    }

    @Test
    public void should_ReturnCourses_When_InstitutionIdIsProvided() throws Exception {
        Course courseOne = Course.builder().institution(institution).build();
        Course courseTwo = Course.builder().institution(institution).build();
        List<Course> courses = Arrays.asList(courseOne, courseTwo);
        courseRepository.saveAll(courses);

        mockMvc.perform(get("/api/v1/institutions/{institution_id}/courses", institution.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnSubjects_When_CourseIdIsProvided() throws Exception {
        Subject subjectOne = Subject.builder().course(course).build();
        Subject subjectTwo = Subject.builder().course(course).build();
        List<Subject> subjects = Arrays.asList(subjectOne, subjectTwo);
        subjectRepository.saveAll(subjects);

        mockMvc.perform(get("/api/v1/courses/{course_id}/subjects", course.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnTopics_When_InstitutionIdIsProvided() throws Exception {
        Topic topicOne = Topic.builder().institution(institution).build();
        Topic topicTwo = Topic.builder().institution(institution).build();
        List<Topic> topics = Arrays.asList(topicOne, topicTwo);
        topicRepository.saveAll(topics);

        mockMvc.perform(get("/api/v1/institutions/{institution_id}/topics", institution.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    // TODO: check for referential integrity violations
    @Test
    public void should_ReturnEducators_When_SubjectIdIsProvided() throws Exception {
        Educator educatorOne = Educator
                .builder()
                .name("educator1")
                .email("educator@gmail.com")
                .userId(UUID.randomUUID())
                .build();
        Educator educatorTwo = Educator
                .builder()
                .name("educator2")
                .email("educator2@gmail.com")
                .userId(UUID.randomUUID()).build();
        List<Educator> educators = Arrays.asList(educatorOne, educatorTwo);
        educatorRepository.saveAll(educators);
        subject.setEducators(educators);
        subject = subjectRepository.save(subject);

        mockMvc.perform(get("/api/v1/subjects/{subject_id}/educators", subject.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnStudents_When_SubjectIdIsProvided() throws Exception {
        Student studentOne = Student
                .builder()
                .name("student1")
                .email("student1@gmail.com")
                .userId(UUID.randomUUID())
                .build();
        Student studentTwo = Student
                .builder()
                .name("student2")
                .email("student2@gmail.com")
                .userId(UUID.randomUUID())
                .build();
        Set<Student> students = Set.of(studentOne, studentTwo);
        studentRepository.saveAll(students);
        subject.setStudents(students);
        subject = subjectRepository.save(subject);

        mockMvc.perform(get("/api/v1/subjects/{subject_id}/students", subject.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_RemoveEducator_When_SubjectIdAndEducatorIdAreProvided() throws Exception {
        educator = Educator
                .builder()
                .name("educator1")
                .email("educator1@gmail.com")
                .userId(UUID.randomUUID())
                .build();
        educator = educatorRepository.save(educator);
        educatorRepository.save(educator);
        subject.setEducators(new ArrayList<>(List.of(educator)));
        subject = subjectRepository.save(subject);

        mockMvc.perform(delete("/api/v1/subjects/{subject_id}/educators/{educator_id}", subject.getId(), educator.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(educator.getName()));
    }

    @Test
    public void should_RemoveStudent_When_SubjectIdAndStudentIdAreProvided() throws Exception {
        student = Student
                .builder()
                .name("student1")
                .email("student1@gmail.com")
                .userId(UUID.randomUUID())
                .build();
        student = studentRepository.save(student);
        subject.setStudents(Set.of(student));
        subject = subjectRepository.save(subject);

        mockMvc.perform(delete("/api/v1/subjects/{subject_id}/students/{student_id}", subject.getId(), student.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(student.getName()));
    }
}
