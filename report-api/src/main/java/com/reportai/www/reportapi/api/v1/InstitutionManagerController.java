package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.services.InstitutionAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/institutions/{institution_id}/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course newCourse, @PathVariable(name = "institution_id") UUID institutionId) {
        Course createdCourse = institutionAdminService.createCourseForInstitution(newCourse, institutionId);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PostMapping("/institutions/{institution_id}/courses/batch")
    public ResponseEntity<List<Course>> createCourses(@RequestBody List<Course> newCourses, @PathVariable(name = "institution_id") UUID institutionId) {
        List<Course> createdCourses = institutionAdminService.createCoursesForInstitution(
            newCourses, institutionId);
        return new ResponseEntity<>(createdCourses, HttpStatus.CREATED);
    }

    @PostMapping("/institutions/{institution_id}/topics")
    public ResponseEntity<Topic> createTopicInInstitution(@RequestBody Topic topic, @PathVariable(name = "institution_id") UUID institutionId) {
        Topic createdTopic = institutionAdminService.createTopicForInstitution(topic,institutionId);
        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    // add subjects to a class
    @PostMapping("/courses/{course_id}/subjects/batch")
    public ResponseEntity<List<Subject>> createSubjectsInCourse(@RequestBody List<Subject> subjects, @PathVariable(name = "course_id") UUID courseId) {
        List<Subject> createdSubjects = institutionAdminService.createSubjectsForCourse(subjects,courseId);
        return new ResponseEntity<>(createdSubjects, HttpStatus.CREATED);
    }

    // add educators to subject
    // only returns educators that are successfully added to the subject
    @PostMapping("/subjects/{subject_id}/educators/batch")
    public ResponseEntity<List<Educator>> addEducatorsToSubject(@RequestBody List<UUID> educatorIds, @PathVariable(name = "subject_id") UUID subjectId) {
        List<Educator> addedEducators = institutionAdminService.addEducatorsToSubject(educatorIds,subjectId);
        return new ResponseEntity<>(addedEducators,HttpStatus.OK);
    }

    // add students to subject
    @PostMapping("/subjects/{subject_id}/students/batch")
    public ResponseEntity<List<Student>> addStudentsToSubject(@RequestBody List<UUID> studentIds, @PathVariable(name = "subject_id") UUID subjectId) {
        List<Student> addedStudents = institutionAdminService.addStudentsToSubject(studentIds,subjectId);
        return new ResponseEntity<>(addedStudents,HttpStatus.OK);
    }
}
