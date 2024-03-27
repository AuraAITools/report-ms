package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.CRUDController;
import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.dtos.requests.CreateStudentRequestBody;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.CRUDService;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/educators")
@Slf4j
public class EducatorController extends SimpleCRUDController<Educator,UUID> {

    private final RegistrationService registrationService;

    public EducatorController(CRUDService<Educator, UUID> service, RegistrationService registrationService) {
        super(service);
        this.registrationService = registrationService;
    }

    @PostMapping("/{educator_id}/class/{class_id}")
    public ResponseEntity<Educator> linkStudentToClass(@PathVariable("educator_id") UUID educatorId, @PathVariable("class_id") UUID classId) {
        Educator educator = registrationService.linkEducatorToClass(educatorId,classId);
        return new ResponseEntity<>(educator, HttpStatus.OK);
    }
}
