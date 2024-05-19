package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.services.InstitutionAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class InstitutionManagerController {

    private final InstitutionAdminService institutionAdminService;

    public InstitutionManagerController(InstitutionAdminService institutionAdminService) {
        this.institutionAdminService = institutionAdminService;
    }

    //Courses
    @GetMapping("/institutions/{institution_id}/courses")
    public ResponseEntity<List<Course>> getCourses(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Course> courses = institutionAdminService.getAllCoursesFromInstitution(institutionId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/institutions/{institution_id}/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course newCourse, @PathVariable(name = "institution_id") UUID institutionId) {
        Course createdCourse = institutionAdminService.createCourseForInstitution(newCourse, institutionId);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PostMapping("/institutions/{institution_id}/courses/batch")
    public ResponseEntity<List<Course>> createCourses(@RequestBody List<Course> newCourses, @PathVariable(name = "institution_id") UUID institutionId) {
        List<Course> createdCourses = institutionAdminService.batchCreateCoursesForInstitution(
            newCourses, institutionId);
        return new ResponseEntity<>(createdCourses, HttpStatus.CREATED);
    }

    @GetMapping("/courses/{course_id}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsInCourse(@PathVariable(name = "course_id") UUID classId) {
        List<Subject> subjects = institutionAdminService.getAllSubjectsFromCourse(classId);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    // add subjects to a course
    @PostMapping("/courses/{course_id}/subjects/batch")
    public ResponseEntity<List<Subject>> createSubjectsInCourse(@RequestBody List<Subject> subjects, @PathVariable(name = "course_id") UUID courseId) {
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
    public ResponseEntity<List<Topic>> getTopicsInInstitution(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Topic> topics = institutionAdminService.getAllTopicsFromInstitution(institutionId);
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    // Subjects
    @GetMapping("/subjects/{subject_id}/educators")
    public ResponseEntity<List<Educator>> getEducatorsFromSubject(@PathVariable(name = "subject_id") UUID subjectId) {
        List<Educator> educators = institutionAdminService.getAllEducatorsFromSubject(subjectId);
        return new ResponseEntity<>(educators, HttpStatus.OK);
    }

    @GetMapping("/subjects/{subject_id}/students")
    public ResponseEntity<List<Student>> getStudentsFromSubject(@PathVariable(name = "subject_id") UUID subjectId) {
        List<Student> students = institutionAdminService.getAllStudentsFromSubject(subjectId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    
    // add educators to subject
    // only returns educators that are successfully added to the subject
    @PostMapping("/subjects/{subject_id}/educators/batch")
    public ResponseEntity<List<Educator>> addEducatorsToSubject(@RequestBody List<UUID> educatorIds, @PathVariable(name = "subject_id") UUID subjectId) {
        List<Educator> addedEducators = institutionAdminService.batchAddEducatorsToSubject(educatorIds,subjectId);
        return new ResponseEntity<>(addedEducators, HttpStatus.OK);
    }

    // add students to subject
    @PostMapping("/subjects/{subject_id}/students/batch")
    public ResponseEntity<List<Student>> addStudentsToSubject(@RequestBody List<UUID> studentIds, @PathVariable(name = "subject_id") UUID subjectId) {
        List<Student> addedStudents = institutionAdminService.batchAddStudentsToSubject(studentIds,subjectId);
        return new ResponseEntity<>(addedStudents, HttpStatus.OK);
    }

    @DeleteMapping("/subjects/{subject_id}/educator/{educator_id}")
    public ResponseEntity<Educator> removeEducatorFromSubject(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "educator_id") UUID educatorId) {
        Educator educator = institutionAdminService.removeEducatorFromSubject(subjectId, educatorId);
        return new ResponseEntity<>(educator, HttpStatus.OK);
    }

    @DeleteMapping("/subjects/{subject_id}/student/{student_id}")
    public ResponseEntity<Student> removeStudentFromSubject(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "student_id") UUID studentId) {
        Student student = institutionAdminService.removeStudentFromSubject(subjectId, studentId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }
}
