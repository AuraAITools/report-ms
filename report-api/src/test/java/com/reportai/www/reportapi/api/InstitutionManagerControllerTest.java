package com.reportai.www.reportapi.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reportai.www.reportapi.api.v1.InstitutionManagerController;
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
import com.reportai.www.reportapi.services.InstitutionAdminService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InstitutionAdminService institutionAdminService;

    @MockBean
    private InstitutionRepository institutionRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private SubjectRepository subjectRepository;

    @MockBean
    private EducatorRepository educatorRepository;

    @MockBean
    private StudentRepository studentRepository;

    @InjectMocks
    private InstitutionManagerController institutionManagerController;

    UUID institutionId = UUID.randomUUID();
    UUID courseId = UUID.randomUUID();
    UUID subjectId = UUID.randomUUID();
    UUID educatorId = UUID.randomUUID();
    UUID studentId = UUID.randomUUID();

    Institution institution;
    Course course;
    Subject subject;
    Educator educator;
    Student student;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        institution = new Institution();
        institution.setId(institutionId);
        when(institutionRepository.findById(any(UUID.class))).thenReturn(Optional.of(institution));

        course = new Course();
        course.setId(courseId);
        when(courseRepository.findById(any(UUID.class))).thenReturn(Optional.of(course));

        subject = new Subject();
        subject.setId(subjectId);
        when(subjectRepository.findById(any(UUID.class))).thenReturn(Optional.of(subject));

        educator = new Educator();
        educator.setId(educatorId);
        when(educatorRepository.findById(any(UUID.class))).thenReturn(Optional.of(educator));

        student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.of(student));
    }

    @Test
    public void should_ReturnCourses_When_InstitutionIdIsProvided() throws Exception {
        Course course1 = new Course();
        Course course2 = new Course();

        List<Course> courses = Arrays.asList(course1, course2);
        institution.setCourses(courses);

        when(institutionAdminService.getAllCoursesFromInstitution(any(UUID.class))).thenReturn(courses);

        mockMvc.perform(get("/api/v1/institutions/{institution_id}/courses", institutionId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnSubjects_When_CourseIdIsProvided() throws Exception {
        Subject subject1 = new Subject();
        Subject subject2 = new Subject();

        List<Subject> subjects = Arrays.asList(subject1, subject2);
        course.setSubjects(subjects);

        when(institutionAdminService.getAllSubjectsFromCourse(any(UUID.class))).thenReturn(subjects);

        mockMvc.perform(get("/api/v1/courses/{course_id}/subjects", courseId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnTopics_When_InstitutionIdIsProvided() throws Exception {
        Topic topic1 = new Topic();
        Topic topic2 = new Topic();

        List<Topic> topics = Arrays.asList(topic1, topic2);
        institution.setTopics(topics);

        when(institutionAdminService.getAllTopicsFromInstitution(any(UUID.class))).thenReturn(topics);

        mockMvc.perform(get("/api/v1/institutions/{institution_id}/topics", institutionId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnEducators_When_SubjectIdIsProvided() throws Exception {
        Educator educator1 = new Educator();
        Educator educator2 = new Educator();

        List<Educator> educators = Arrays.asList(educator1, educator2);
        subject.setEducators(educators);

        when(institutionAdminService.getAllEducatorsFromSubject(any(UUID.class))).thenReturn(educators);

        mockMvc.perform(get("/api/v1/subjects/{subject_id}/educators", subjectId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_ReturnStudents_When_SubjectIdIsProvided() throws Exception {
        Student student1 = new Student();
        Student student2 = new Student();

        List<Student> students = Arrays.asList(student1, student2);
        subject.setStudents(students);

        when(institutionAdminService.getAllStudentsFromSubject(any(UUID.class))).thenReturn(students);

        mockMvc.perform(get("/api/v1/subjects/{subject_id}/students", subjectId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void should_RemoveEducator_When_SubjectIdAndEducatorIdAreProvided() throws Exception {
        Educator educator = new Educator();
        educator.setId(educatorId);

        subject.setEducators(new ArrayList<>(List.of(educator)));
        subjectRepository.save(subject);
        when(institutionAdminService.removeEducatorFromSubject(any(UUID.class), any(UUID.class))).thenReturn(educator);

        mockMvc.perform(delete("/api/v1/subjects/{subject_id}/educator/{educator_id}", subjectId, educatorId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(educatorId.toString()));
    }

    @Test
    public void should_RemoveStudent_When_SubjectIdAndStudentIdAreProvided() throws Exception {
        Student student = new Student();
        student.setId(studentId);

        subject.setStudents(new ArrayList<>(List.of(student)));
        subjectRepository.save(subject);
        when(institutionAdminService.removeStudentFromSubject(any(UUID.class), any(UUID.class))).thenReturn(student);

        mockMvc.perform(delete("/api/v1/subjects/{subject_id}/student/{student_id}", subjectId, studentId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(studentId.toString()));
    }
}
