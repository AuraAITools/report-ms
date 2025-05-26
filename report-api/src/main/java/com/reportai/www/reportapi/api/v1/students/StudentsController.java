package com.reportai.www.reportapi.api.v1.students;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.students.requests.UpdateStudentRequestDTO;
import com.reportai.www.reportapi.api.v1.students.responses.ExpandedStudentResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ModelMapper modelMapper;

    @Autowired
    public StudentsController(OutletsService outletsService, StudentsService studentsService, ModelMapper modelMapper) {
        this.outletsService = outletsService;
        this.studentsService = studentsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "get all students in an outlet", description = "get all students in an outlet")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::students:read'")
    @Transactional
    public ResponseEntity<List<StudentResponseDTO>> getStudentsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Student> students = outletsService.getOutletStudents(outletId);
        return new ResponseEntity<>(students.stream().map(student -> modelMapper.map(student, StudentResponseDTO.class)).toList(), HttpStatus.OK);
    }

    @Operation(summary = "get all expanded students in an outlet", description = "get all expanded students in an outlet")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/students/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::students:read'")
    @Transactional
    public ResponseEntity<List<ExpandedStudentResponseDTO>> getExpandedStudentsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Student> students = outletsService.getOutletStudents(outletId);
        return new ResponseEntity<>(students.stream().map(student -> modelMapper.map(student, ExpandedStudentResponseDTO.class)).toList(), HttpStatus.OK);
    }

    @PatchMapping("/institutions/{id}/students/{student_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:update'")
    @Transactional
    public ResponseEntity<StudentResponseDTO> updateStudents(@PathVariable UUID id, @PathVariable(name = "student_id") UUID studentId, @RequestBody @Valid UpdateStudentRequestDTO updateStudentRequestDTO) {
        Student student = studentsService.update(studentId, updateStudentRequestDTO);
        return new ResponseEntity<>(modelMapper.map(student, StudentResponseDTO.class), HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:read'")
    @Transactional
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(@PathVariable UUID id) {
        Collection<Student> students = studentsService.getAllStudentsInInstitution(id);
        return new ResponseEntity<>(students.stream().map(student -> modelMapper.map(student, StudentResponseDTO.class)).toList(), HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/students/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:read'")
    @Transactional
    public ResponseEntity<List<ExpandedStudentResponseDTO>> getAllExpandedStudents(@PathVariable UUID id) {
        Collection<Student> students = studentsService.getAllStudentsInInstitution(id);
        return new ResponseEntity<>(students.stream().map(student -> modelMapper.map(student, ExpandedStudentResponseDTO.class)).toList(), HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/students/{student_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::students:read'")
    @Transactional
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID id, @PathVariable(name = "student_id") UUID studentId) {
        Student student = studentsService.findById(studentId);
        return new ResponseEntity<>(modelMapper.map(student, StudentResponseDTO.class), HttpStatus.OK);
    }
}
