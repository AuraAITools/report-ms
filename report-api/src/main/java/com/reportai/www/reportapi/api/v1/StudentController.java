package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.dtos.requests.CreateStudentRequestBody;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.CRUDService;
import com.reportai.www.reportapi.services.RegistrationService;
import com.reportai.www.reportapi.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
public class StudentController extends SimpleCRUDController<Student,UUID> {
    private final RegistrationService registrationService;

    public StudentController(CRUDService<Student, UUID> service, RegistrationService registrationService) {
        super(service);
        this.registrationService = registrationService;
    }

    @PostMapping("/{student_id}/class/{class_id}")
    public ResponseEntity<Student> linkStudentToClass(@PathVariable("student_id") UUID studentId, @PathVariable("class_id") UUID classId) {
        Student student = registrationService.linkStudentToClass(studentId,classId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

}
