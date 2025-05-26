package com.reportai.www.reportapi.api.v1.schools;

import com.reportai.www.reportapi.api.v1.schools.responses.SchoolResponseDTO;
import com.reportai.www.reportapi.services.schools.SchoolsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Outlet APIs", description = "APIs for managing a Outlet resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class SchoolsController {

    private final SchoolsService schoolsService;

    private final ModelMapper modelMapper;

    @Autowired
    public SchoolsController(SchoolsService schoolsService, ModelMapper modelMapper) {
        this.schoolsService = schoolsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "get all outlets for a institution", description = "get all outlets for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/schools")
    // TODO: add permission check
    public ResponseEntity<List<SchoolResponseDTO>> getAllSchools(@PathVariable(name = "id") String id) {
        List<SchoolResponseDTO> schoolResponseDTOS = schoolsService
                .findAll()
                .stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .toList();
        return new ResponseEntity<>(schoolResponseDTOS, HttpStatus.OK);
    }
}
