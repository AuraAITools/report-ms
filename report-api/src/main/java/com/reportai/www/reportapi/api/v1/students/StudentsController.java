package com.reportai.www.reportapi.api.v1.students;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentResponseDTO;
import com.reportai.www.reportapi.api.v1.students.requests.UpdateStudentRequestDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final TenantAwareAccountsService tenantAwareAccountsService;

    @Autowired
    public StudentsController(OutletsService outletsService, StudentsService studentsService, CoursesService coursesService, TenantAwareAccountsService tenantAwareAccountsService) {
        this.outletsService = outletsService;
        this.studentsService = studentsService;
        this.coursesService = coursesService;
        this.tenantAwareAccountsService = tenantAwareAccountsService;
    }

    @Operation(summary = "creates a student in a client account", description = "creates student in an already created client account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-student'")
    @Transactional
    public ResponseEntity<StudentResponseDTO> createStudentInClientAccount(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateStudentRequestDTO createStudentRequestDTO) {

        Student newStudent = StudentMappers.convert(createStudentRequestDTO, id);
        Student createdStudent = tenantAwareAccountsService.createStudentInTenantAwareAccount(accountId, newStudent);
        studentsService.addLevel(createdStudent.getId(), createStudentRequestDTO.getLevelId());
        if (!createStudentRequestDTO.getCourseIds().isEmpty()) {
            List<Course> courses = coursesService.findByIds(createStudentRequestDTO.getCourseIds());
            courses.forEach(course -> coursesService.registerStudentsToCourse(course.getId(), List.of(createdStudent.getId())));
        }
        return new ResponseEntity<>(StudentMappers.convert(createdStudent), HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::students:read'")
    @Transactional
    public ResponseEntity<List<StudentResponseDTO>> getStudentsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Student> students = outletsService.getOutletStudents(outletId);
        return new ResponseEntity<>(students.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }

    @PatchMapping("/institutions/{id}/students/{student_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:update'")
    @Transactional
    public ResponseEntity<StudentResponseDTO> updateStudentsFromInstitution(@PathVariable UUID id, @PathVariable(name = "student_id") UUID studentId, @RequestBody @Valid UpdateStudentRequestDTO updateStudentRequestDTO) {
        Student student = studentsService.update(studentId, updateStudentRequestDTO);
        return new ResponseEntity<>(StudentMappers.convert(student), HttpStatus.OK);
    }

    // TODO: finish this API
    @GetMapping("/institutions/{id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:read'")
    @Transactional
    public ResponseEntity<List<StudentResponseDTO>> getAllStudentsInInstitution(@PathVariable UUID id) {
        Collection<Student> students = studentsService.getAllStudentsInInstitution(id);
        return new ResponseEntity<>(students.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }
}
