package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.dtos.requests.CreateInstitutionRequestBody;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.services.CRUDService;
import com.reportai.www.reportapi.services.InstitutionService;
import com.reportai.www.reportapi.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/institutions")
@Slf4j
public class InstitutionController extends SimpleCRUDController<Institution, UUID> {

    public InstitutionController(InstitutionService service) {
        super(service);
    }

    @PostMapping("/{id}/parents")
    public ResponseEntity<Parent> createParentInInstitution(@RequestParam UUID id, @RequestBody Parent parent) {
        Institution institution = service.findById(id);
        institution.getParents().add(parent);
        Institution updatedInstitution = service.update(institution);

        Parent createdParent = updatedInstitution.getParents()
                .stream()
                .filter(p -> p.getUser().getName().equals(parent.getUser().getName()))
                .toList()
                .getFirst();
        return new ResponseEntity<>(createdParent,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/educators")
    public ResponseEntity<Educator> createEducatorInInstitution(@RequestParam UUID id, @RequestBody Educator educator) {
        Institution institution = service.findById(id);
        institution.getEducators().add(educator);
        Institution updatedInstitution = service.update(institution);

        Educator createdEducator = updatedInstitution.getEducators()
                .stream()
                .filter(p -> p.getUser().getName().equals(educator.getUser().getName()))
                .toList()
                .getFirst();
        return new ResponseEntity<>(createdEducator,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/students")
    public ResponseEntity<Student> createStudentInInstitution(@RequestParam UUID id, @RequestBody Student student) {
        Institution institution = service.findById(id);
        institution.getStudents().add(student);
        Institution updatedInstitution = service.update(institution);

        Student createdStudent = updatedInstitution.getStudents()
                .stream()
                .filter(p -> p.getUser().getName().equals(student.getUser().getName()))
                .toList()
                .getFirst();
        return new ResponseEntity<>(createdStudent,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/subjects")
    public ResponseEntity<Subject> createSubjectInInstitution(@RequestParam UUID id, @RequestBody Subject subject) {
        Institution institution = service.findById(id);
        institution.getSubjects().add(subject);
        Institution updatedInstitution = service.update(institution);

        Subject createdSubject = updatedInstitution.getSubjects()
                .stream()
                .filter(p -> p.getName().equals(subject.getName()))
                .toList()
                .getFirst();
        return new ResponseEntity<>(createdSubject,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/classes")
    public ResponseEntity<Class> createClassInInstitution(@RequestParam UUID id, @RequestBody Class newClass) {
        Institution institution = service.findById(id);
        institution.getClasses().add(newClass);
        Institution updatedInstitution = service.update(institution);

        Class createdClass = updatedInstitution.getClasses()
                .stream()
                .filter(p -> p.getName().equals(newClass.getName()))
                .toList()
                .getFirst();
        return new ResponseEntity<>(createdClass,HttpStatus.CREATED);
    }
}
