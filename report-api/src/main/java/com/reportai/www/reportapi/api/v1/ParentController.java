package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.dtos.requests.CreateParentRequestBody;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parent")
@Slf4j
public class ParentController {
    private final RegistrationService registrationService;

    public ParentController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Parent> createParentWithInstitution(@RequestBody CreateParentRequestBody createParentRequestBody) {
        Parent parent = registrationService.registerParentWithInstitution(
                Parent.builder().build(),
                createParentRequestBody.getUser().toEntity(),
                createParentRequestBody.getInstitutionId()
        );

        return new ResponseEntity<>(parent, HttpStatus.CREATED);
    }

}
