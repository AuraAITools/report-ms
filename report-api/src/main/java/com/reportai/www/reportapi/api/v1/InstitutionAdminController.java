package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.services.InstitutionAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class InstitutionAdminController {

    private final InstitutionAdminService institutionAdminService;

    public InstitutionAdminController(InstitutionAdminService institutionAdminService) {
        this.institutionAdminService = institutionAdminService;
    }

    //Courses
    @GetMapping("/institutions/{institution_id}/courses")
    public ResponseEntity<List<Course>> getAllCourses(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Course> courses = institutionAdminService.getAllCoursesFromInstitution(institutionId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/institutions/{institution_id}/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course newCourse, @PathVariable(name = "institution_id") UUID institutionId) {
        Course createdCourse = institutionAdminService.createCourseForInstitution(newCourse, institutionId);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PostMapping("/institutions/{institution_id}/courses/batch")
    public ResponseEntity<List<Course>> batchCreateCourses(@RequestBody List<Course> newCourses, @PathVariable(name = "institution_id") UUID institutionId) {
        List<Course> createdCourses = institutionAdminService.batchCreateCoursesForInstitution(
            newCourses, institutionId);
        return new ResponseEntity<>(createdCourses, HttpStatus.CREATED);
    }

    @GetMapping("/courses/{course_id}/subjects")
    public ResponseEntity<List<Subject>> getAllSubjectsInCourse(@PathVariable(name = "course_id") UUID classId) {
        List<Subject> subjects = institutionAdminService.getAllSubjectsFromCourse(classId);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    // add subjects to a course
    //TODO: add authorization to every controller method
    @PreAuthorize("hasRole('INST')")
    @PostMapping("/courses/{course_id}/subjects/batch")
    public ResponseEntity<List<Subject>> batchCreateSubjectsInCourse(@RequestBody List<Subject> subjects, @PathVariable(name = "course_id") UUID courseId) {
        List<Subject> createdSubjects = institutionAdminService.batchCreateSubjectsForCourse(subjects,courseId);
        return new ResponseEntity<>(createdSubjects, HttpStatus.CREATED);
    }

    //Topics
    @PostMapping("/institutions/{institution_id}/topics")
    public ResponseEntity<Topic> createTopicInInstitution(@RequestBody Topic topic, @PathVariable(name = "institution_id") UUID institutionId) {
        Topic createdTopic = institutionAdminService.createTopicForInstitution(topic,institutionId);
        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{institution_id}/topics")
    public ResponseEntity<List<Topic>> getAllTopicsInInstitution(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Topic> topics = institutionAdminService.getAllTopicsFromInstitution(institutionId);
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    // Subjects
    @GetMapping("/subjects/{subject_id}/educators")
    public ResponseEntity<List<Educator>> getAllEducatorsFromSubject(@PathVariable(name = "subject_id") UUID subjectId) {
        List<Educator> educators = institutionAdminService.getAllEducatorsFromSubject(subjectId);
        return new ResponseEntity<>(educators, HttpStatus.OK);
    }

    @GetMapping("/subjects/{subject_id}/students")
    public ResponseEntity<List<Student>> getAllStudentsFromSubject(@PathVariable(name = "subject_id") UUID subjectId) {
        List<Student> students = institutionAdminService.getAllStudentsFromSubject(subjectId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    
    // add educators to subject
    // only returns educators that are successfully added to the subject
    @PostMapping("/subjects/{subject_id}/educators/batch")
    public ResponseEntity<List<Educator>> batchAddEducatorsToSubject(@RequestBody List<UUID> educatorIds, @PathVariable(name = "subject_id") UUID subjectId) {
        List<Educator> addedEducators = institutionAdminService.batchAddEducatorsToSubject(educatorIds,subjectId);
        return new ResponseEntity<>(addedEducators, HttpStatus.OK);
    }

    // add students to subject
    @PostMapping("/subjects/{subject_id}/students/batch")
    public ResponseEntity<List<Student>> batchAddStudentsToSubject(@RequestBody List<UUID> studentIds, @PathVariable(name = "subject_id") UUID subjectId) {
        List<Student> addedStudents = institutionAdminService.batchAddStudentsToSubject(studentIds,subjectId);
        return new ResponseEntity<>(addedStudents, HttpStatus.OK);
    }

    @DeleteMapping("/subjects/{subject_id}/educators/{educator_id}")
    public ResponseEntity<Educator> removeEducatorFromSubject(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "educator_id") UUID educatorId) {
        Educator educator = institutionAdminService.removeEducatorFromSubject(subjectId, educatorId);
        return new ResponseEntity<>(educator, HttpStatus.OK);
    }

    @DeleteMapping("/subjects/{subject_id}/students/{student_id}")
    public ResponseEntity<Student> removeStudentFromSubject(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "student_id") UUID studentId) {
        Student student = institutionAdminService.removeStudentFromSubject(subjectId, studentId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PatchMapping("/institutions/{institution_id}/educators/{educator_id}")
    public ResponseEntity<Educator> registerEducatorToInstitution(@PathVariable(name = "institution_id") UUID institutionId, @PathVariable(name = "educator_id") UUID educatorId) {
        Educator registeredEducator = institutionAdminService.registerEducatorToInstitution(institutionId,educatorId);
        return new ResponseEntity<>(registeredEducator, HttpStatus.OK);
    }

    @PatchMapping("/institutions/{institution_id}/students/{student_id}")
    public ResponseEntity<Student> registerStudentToInstitution(@PathVariable(name = "institution_id") UUID institutionId, @PathVariable(name = "student_id") UUID studentId) {
        Student registeredStudent = institutionAdminService.registerStudentToInstitution(institutionId,studentId);
        return new ResponseEntity<>(registeredStudent, HttpStatus.OK);
    }

    @PatchMapping("/institutions/{institution_id}/parents/{parent_id}")
    public ResponseEntity<Parent> registerParentToInstitution(@PathVariable(name = "institution_id") UUID institutionId, @PathVariable(name = "parent_id") UUID parentId) {
        Parent registeredParent = institutionAdminService.registerParentToInstitution(institutionId,parentId);
        return new ResponseEntity<>(registeredParent, HttpStatus.OK);
    }
}
