package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.dtos.requests.CreateInstitutionRequestBody;
import com.reportai.www.reportapi.services.CRUDService;
import com.reportai.www.reportapi.services.InstitutionService;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/institutions")
@Slf4j
public class InstitutionController extends SimpleCRUDController<Institution, UUID> {

    public InstitutionController(InstitutionService service) {
        super(service);
    }
}
