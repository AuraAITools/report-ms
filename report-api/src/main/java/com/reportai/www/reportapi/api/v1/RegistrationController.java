package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.RegistrationService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Student> registerStudentToParent(@PathVariable(name = "parent_id")UUID parentId, @PathVariable(name = "student_id") UUID studentId) {
        Student registeredStudent = registrationService.registerStudentToParent(studentId,parentId);
        return new ResponseEntity<>(registeredStudent, HttpStatus.OK);
    }
}
