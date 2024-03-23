package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.dtos.requests.CreateSubjectRequestBody;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subject")
@Slf4j
public class SubjectController {
    private final RegistrationService registrationService;

    public SubjectController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody CreateSubjectRequestBody createSubjectRequestBody) {
        Subject subject = registrationService.registerSubjectWithInstitution(
                Subject.builder().build(),
                createSubjectRequestBody.getInstitutionId()
        );
        return new ResponseEntity<>(subject, HttpStatus.CREATED);
    }

}
