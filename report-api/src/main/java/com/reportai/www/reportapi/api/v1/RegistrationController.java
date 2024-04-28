package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PatchMapping("/parents/{parent_id}/students/{student_id}/register")
    public ResponseEntity<Student> registerStudentToParent(@PathVariable(name = "parent_id") UUID parentId, @PathVariable(name = "student_id") UUID studentId) {
        Student registeredStudent = registrationService.registerStudentToParent(studentId, parentId);
        return new ResponseEntity<>(registeredStudent, HttpStatus.OK);
    }

    @PatchMapping("/educators/{educator_id}/institutions/{institution_id}/register")
    public ResponseEntity<Educator> registerEducatorToInstitution(@PathVariable(name = "educator_id") UUID educatorId, @PathVariable(name = "institution_id") UUID institutionId) {
        Educator educator = registrationService.registerEducatorToInstitution(educatorId, institutionId);
        return new ResponseEntity<>(educator, HttpStatus.OK);
    }

    @PatchMapping("/parents/{parent_id}/institutions/{institution_id}/register")
    public ResponseEntity<Parent> registerParentToInstitution(@PathVariable(name = "parent_id") UUID parentId, @PathVariable(name = "institution_id") UUID institutionId) {
        Parent registeredParent = registrationService.registerParentToInstitution(parentId, institutionId);
        return new ResponseEntity<>(registeredParent, HttpStatus.OK);
    }

    @PatchMapping("/classes/{class_id}/institutions/{institution_id}/register")
    public ResponseEntity<Class> registerClassToInstitution(@PathVariable(name = "class_id") UUID classId, @PathVariable(name = "institution_id") UUID institutionId) {
        Class registeredClass = registrationService.registerClassToInstitution(classId, institutionId);
        return new ResponseEntity<>(registeredClass, HttpStatus.OK);
    }

    @PatchMapping("/students/{student_id}/classes/{class_id}/register")
    public ResponseEntity<Student> registerStudentToClass(@PathVariable(name = "student_id") UUID studentId, @PathVariable(name = "class_id") UUID classId) {
        Student registeredStudent = registrationService.registerStudentToClass(studentId, classId);
        return new ResponseEntity<>(registeredStudent, HttpStatus.OK);
    }

    @PatchMapping("/subjects/{subject_id}/classes/{class_id}/register")
    public ResponseEntity<Subject> registerSubjectToClass(@PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "class_id") UUID classId) {
        Subject registeredSubject = registrationService.registerSubjectToClass(subjectId, classId);
        return new ResponseEntity<>(registeredSubject, HttpStatus.OK);
    }
}
