package com.reportai.www.reportapi.api.v1.subjects;

import com.reportai.www.reportapi.api.v1.subjects.requests.CreateSubjectDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.mappers.SubjectMappers;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.reportai.www.reportapi.mappers.SubjectMappers.convert;

@Tag(name = "Subjects APIs", description = "APIs for managing a Subjects resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class SubjectsController {

    private final SubjectsService subjectsService;

    public SubjectsController(SubjectsService subjectsService) {
        this.subjectsService = subjectsService;
    }

    // Subjects
    @GetMapping("/institutions/{id}/subjects")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjectsForInstitution(@PathVariable UUID id) {
        List<Subject> subjects = subjectsService.getAllSubjectsForInstitution(id);
        List<SubjectResponseDTO> subjectResponseDTOS = subjects
                .stream()
                .map(SubjectMappers::convert)
                .toList();
        return new ResponseEntity<>(subjectResponseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "create a subject for a institution", description = "create an subject for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/subjects")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<SubjectResponseDTO> createSubjectForInstitution(@PathVariable UUID id, @Valid @RequestBody CreateSubjectDTO createSubjectDTO) {
        Subject createdSubject = subjectsService.createSubjectForInstitution(id, convert(createSubjectDTO, id));
        return new ResponseEntity<>(convert(createdSubject), HttpStatus.OK);
    }

}
