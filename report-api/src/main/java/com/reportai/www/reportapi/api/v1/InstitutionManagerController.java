package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Class;
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

    //Classes
    @PostMapping("/institutions/{institution_id}/classes")
    public ResponseEntity<Class> createClass(@RequestBody Class newClass, @PathVariable(name = "institution_id") UUID institutionId) {
        Class createdClass = institutionAdminService.createClassForInstitution(newClass, institutionId);
        return new ResponseEntity<>(createdClass, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{institution_id}/classes")
    public ResponseEntity<List<Class>> getClasses(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Class> classes = institutionAdminService.getClassesFromInstitution(institutionId);
        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @PostMapping("/institutions/{institution_id}/classes/batch")
    public ResponseEntity<List<Class>> createClasses(@RequestBody List<Class> newClasses, @PathVariable(name = "institution_id") UUID institutionId) {
        List<Class> createdClasses = institutionAdminService.createClassesForInstitution(newClasses, institutionId);
        return new ResponseEntity<>(createdClasses, HttpStatus.CREATED);
    }

    // add subjects to a class
    @PostMapping("/classes/{class_id}/subjects/batch")
    public ResponseEntity<List<Subject>> createSubjectsInClass(@RequestBody List<Subject> subjects, @PathVariable(name = "class_id") UUID classId) {
        List<Subject> createdSubjects = institutionAdminService.createSubjectsForClass(subjects,classId);
        return new ResponseEntity<>(createdSubjects, HttpStatus.CREATED);
    }

    @GetMapping("/classes/{class_id}/subjects")
    public ResponseEntity<List<Subject>> getSubjectsInClass(@PathVariable(name = "class_id") UUID classId) {
        List<Subject> subjects = institutionAdminService.getSubjectsFromClass(classId);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    //Topics
    @PostMapping("/institutions/{institution_id}/topics")
    public ResponseEntity<Topic> createTopicInInstitution(@RequestBody Topic topic, @PathVariable(name = "institution_id") UUID institutionId) {
        Topic createdTopic = institutionAdminService.createTopicForInstitution(topic,institutionId);
        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{institution_id}/topics")
    public ResponseEntity<List<Topic>> getTopicsInInstitution(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Topic> topics = institutionAdminService.getTopicsFromInstitution(institutionId);
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    // Subjects
    @GetMapping("/subjects/{subject_id}/educators")
    public ResponseEntity<List<Educator>> getEducatorsFromSubject(@PathVariable(name = "subject_id") UUID subjectId) {
        List<Educator> educators = institutionAdminService.getEducatorsFromSubject(subjectId);
        return new ResponseEntity<>(educators, HttpStatus.OK);
    }

    @GetMapping("/subjects/{subject_id}/students")
    public ResponseEntity<List<Student>> getStudentsFromSubject(@PathVariable(name = "subject_id") UUID subjectId) {
        List<Student> students = institutionAdminService.getStudentsFromSubject(subjectId);
        return new ResponseEntity<>(students, HttpStatus.OK);
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

    @DeleteMapping("/subjects/{subject_id}/educator/{educator_id}")
    public ResponseEntity<Educator> removeEducatorFromSubject(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "educator_id") UUID educatorId) {
        Educator educator = institutionAdminService.removeEducatorFromSubject(subjectId, educatorId);
        return new ResponseEntity<>(educator,HttpStatus.OK);
    }

    @DeleteMapping("/subjects/{subject_id}/educator/{student_id}")
    public ResponseEntity<Student> removeStudentFromSubject(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "student_id") UUID educatorId) {
        Student student = institutionAdminService.removeStudentFromSubject(subjectId, educatorId);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }
}
