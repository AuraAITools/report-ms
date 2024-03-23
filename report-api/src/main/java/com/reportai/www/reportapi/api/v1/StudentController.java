package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.dtos.requests.CreateStudentRequestBody;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
@Slf4j
public class StudentController {
    private final RegistrationService registrationService;

    public StudentController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentRequestBody createStudentRequestBody) {
        Student student = registrationService.registerStudentWithInstitution(
                Student.builder().build(),
                createStudentRequestBody.getUser().toEntity(),
                createStudentRequestBody.getInstitutionId()
        );

        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

}
