package com.reportai.www.reportapi.api.v1.students;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Students APIs", description = "APIs for querying students of institutions")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class StudentsController {

    private final OutletsService outletsService;
    private final StudentsService studentsService;
    private final CoursesService coursesService;

    @Autowired
    public StudentsController(OutletsService outletsService, StudentsService studentsService, CoursesService coursesService) {
        this.outletsService = outletsService;
        this.studentsService = studentsService;
        this.coursesService = coursesService;
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::students:read'")
    @Transactional
    public ResponseEntity<List<CreateStudentResponseDTO>> getStudentsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        List<Student> students = outletsService.getOutletStudents(outletId);
        return new ResponseEntity<>(students.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }

    // TODO: finish this API
    @GetMapping("/institutions/{id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:read'")
    @Transactional
    public ResponseEntity<List<CreateStudentResponseDTO>> getAllStudentsInInstitution(@PathVariable UUID id) {
        List<Student> students = studentsService.getAllStudentsInInstitution(id);
        return new ResponseEntity<>(students.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }
}
