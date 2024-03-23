package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.dtos.requests.CreateClassRequestBody;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/class")
@Slf4j
public class ClassController {

    private final RegistrationService registrationService;

    public ClassController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Class> createClassWithInstitution(@RequestBody CreateClassRequestBody createClassRequestBody) {
        Class newClass = registrationService.registerClassWithInstitution(
                createClassRequestBody.toEntity(),
                createClassRequestBody.getInstitutionId()
        );
        return new ResponseEntity<>(newClass, HttpStatus.CREATED);
    }

}
