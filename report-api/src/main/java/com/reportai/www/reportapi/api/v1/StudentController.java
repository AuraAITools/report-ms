package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.commons.CRUDService;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
public class StudentController extends SimpleCRUDController<Student,UUID> {

    public StudentController(CRUDService<Student, UUID> service, RegistrationService registrationService) {
        super(service);
    }


}
