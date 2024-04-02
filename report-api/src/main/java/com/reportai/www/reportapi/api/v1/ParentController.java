package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.commons.CRUDService;
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
@RequestMapping("/api/v1/parents")
@Slf4j
public class ParentController extends SimpleCRUDController<Parent,UUID> {
    private final RegistrationService registrationService;

    public ParentController(CRUDService<Parent, UUID> service, RegistrationService registrationService) {
        super(service);
        this.registrationService = registrationService;
    }

    @PostMapping("/{id}/students")
    public ResponseEntity<Student> addStudentToParent(@PathVariable UUID id, @RequestBody Student student) {
        Parent parent = service.findById(id);
        parent.getStudents().add(student);
        Parent updatedParent = service.update(parent);

        Student createdStudent = updatedParent.getStudents()
                .stream()
                .filter(s -> s.getUser().getName().equals(student.getUser().getName()))
                .toList()
                .getFirst();
        return new ResponseEntity<>(createdStudent , HttpStatus.CREATED);
    }
}
