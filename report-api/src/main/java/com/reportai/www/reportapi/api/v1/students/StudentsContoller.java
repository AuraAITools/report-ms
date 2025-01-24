package com.reportai.www.reportapi.api.v1.students;


import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Student APIs", description = "APIs for managing a Student resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class StudentsContoller {

    private final StudentsService studentsService;

    public StudentsContoller(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

//    @PostMapping("/institutions/{id}/students")
//    public ResponseEntity<CreateStudentResponseDTO> createStudent(@PathVariable UUID id, @RequestBody CreateStudentDTO) {
//        studentsService.createStudent()
//    }
}
