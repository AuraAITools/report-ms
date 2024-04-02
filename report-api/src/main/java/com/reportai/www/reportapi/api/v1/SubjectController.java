package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.services.commons.CRUDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subjects")
@Slf4j
public class SubjectController extends SimpleCRUDController<Subject, UUID> {

    public SubjectController(CRUDService<Subject, UUID> service) {
        super(service);
    }
}
