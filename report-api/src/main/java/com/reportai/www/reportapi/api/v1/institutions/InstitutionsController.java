package com.reportai.www.reportapi.api.v1.institutions;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.annotations.authorisation.HasRole;
import com.reportai.www.reportapi.api.v1.institutions.requests.CreateInstitutionRequestDTO;
import com.reportai.www.reportapi.api.v1.institutions.requests.PatchInstitutionRequestDTO;
import com.reportai.www.reportapi.api.v1.institutions.responses.InstitutionResponseDTO;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.mappers.InstitutionMappers;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Institution Admin APIs", description = "APIs for managing institutions resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class InstitutionsController {

    private final InstitutionsService institutionsService;

    private final ModelMapper modelMapper;

    @Autowired
    public InstitutionsController(InstitutionsService institutionsService, ModelMapper modelMapper) {
        this.institutionsService = institutionsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "get institution details", description = "create an account for the clients of a institution.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}")
    @HasResourcePermission(permission = "'institutions::' + #id + ':read'")
    public ResponseEntity<InstitutionResponseDTO> getInstitution(@PathVariable UUID id) {
        return new ResponseEntity<>(modelMapper.map(institutionsService.getInstitutionFromContext(), InstitutionResponseDTO.class), HttpStatus.OK);
    }

    @Operation(summary = "patch details of a institution", description = "this endpoint is used to fully fill in institution details when onboarding")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}")
    @HasResourcePermission(permission = "'institutions::' + #id + ':update'")
    @Transactional
    public ResponseEntity<InstitutionResponseDTO> updateInstitutionById(@PathVariable UUID id, @RequestBody @Valid PatchInstitutionRequestDTO patchInstitutionRequestDTO) {
        Institution incomingUpdate = InstitutionMappers.convert(patchInstitutionRequestDTO, id.toString());
        institutionsService.update(incomingUpdate);
        return new ResponseEntity<>(modelMapper.map(incomingUpdate, InstitutionResponseDTO.class), HttpStatus.OK);
    }

    @Operation(summary = "creates an institution", description = "creates an bare institution with no accounts. Please create an admin account on the institution to continue")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions")
    @HasRole("'aura-admin'")
    @Transactional
    public ResponseEntity<InstitutionResponseDTO> createInstitution(@RequestBody @Valid CreateInstitutionRequestDTO createInstitutionRequestDTO) {
        Institution institution = modelMapper.map(createInstitutionRequestDTO, Institution.class);
        institutionsService.create(institution);
        return new ResponseEntity<>(modelMapper.map(institution, InstitutionResponseDTO.class), HttpStatus.OK);
    }
}
