package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.dtos.requests.CreateInstitutionRequestBody;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/institution")
@Slf4j
public class InstitutionController {

    private final RegistrationService registrationService;

    public InstitutionController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Institution> createInstitution(@RequestBody CreateInstitutionRequestBody createInstitutionRequestBody) {
        Institution institution = registrationService.registerInstitution(
                Institution.builder().build(),
                createInstitutionRequestBody.getUser().toEntity()
        );

        return new ResponseEntity<>(institution, HttpStatus.CREATED);
    }
}
