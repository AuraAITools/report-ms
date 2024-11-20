package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.dtos.requests.CreateClientAccountDTO;
import com.reportai.www.reportapi.dtos.requests.CreateOutletDTO;
import com.reportai.www.reportapi.dtos.requests.PatchInstitutionRequestDTO;
import com.reportai.www.reportapi.dtos.responses.InstitutionResponseDto;
import com.reportai.www.reportapi.dtos.responses.OutletResponseDto;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.mappers.InstitutionMappers;
import com.reportai.www.reportapi.mappers.OutletMappers;
import com.reportai.www.reportapi.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

@Tag(name = "Institution Admin APIs", description = "APIs for administrators of institutions")
@RestController
@RequestMapping("/api/v1")
@Validated
public class InstitutionAdminController {

    private final AdminService adminService;

    @Autowired
    public InstitutionAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "create an account for the clients of a institution", description = "create an account for the clients of a institution. A client of a institution is defined as the person who is registering with the institution, it could be either a student or a parent. The client will be able to log in to the Aura Report mobile app after email verification")
    @ApiResponse(responseCode = "201", description = "created")
    @PostMapping("/institutions/{id}/accounts")
    public ResponseEntity<Void> createClientAccount(@PathVariable UUID id, @RequestBody @Valid CreateClientAccountDTO createClientAccountDTO) {
        String createdUserId = adminService.createStudentAccountForInstitution(createClientAccountDTO, id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("user_id", createdUserId);
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @Operation(summary = "get details of a institution", description = "get details of a institution by ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}")
    public ResponseEntity<InstitutionResponseDto> getInstitutionById(@PathVariable UUID id) {
        Institution institution = adminService.getInstitutionById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        InstitutionResponseDto response = InstitutionMappers.convert(institution);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "patch details of a institution", description = "this endpoint is used to fully fill in institution details when onboarding")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}")
    public ResponseEntity<InstitutionResponseDto> updateInstitutionById(@PathVariable UUID id, @RequestBody @Valid PatchInstitutionRequestDTO patchInstitutionRequestDTO) {
        Institution incomingUpdate = InstitutionMappers.convert(patchInstitutionRequestDTO);
        Institution patchedInstitution = adminService.updateInstitution(id, incomingUpdate);
        return new ResponseEntity<>(InstitutionMappers.convert(patchedInstitution), HttpStatus.OK);
    }

    @Operation(summary = "create a outlet for a institution", description = "create an outlet for a institution")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @PostMapping("/institutions/{id}/outlets")
    public ResponseEntity<OutletResponseDto> createOutletForInstitution(@PathVariable UUID id, @Valid @RequestBody CreateOutletDTO createOutletDTO) {
        Outlet newOutlet = OutletMappers.convert(createOutletDTO);
        Outlet createdOutlet = adminService.createOutletForInstitution(id, newOutlet);
        OutletResponseDto outletResponseDto = OutletMappers.convert(createdOutlet);
        return new ResponseEntity<>(outletResponseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "get all outlets for a institution", description = "get all outlets for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets")
    public ResponseEntity<List<OutletResponseDto>> createOutletForInstitution(@PathVariable UUID id) {
        List<Outlet> outlets = adminService.getAllOutletsForInstitution(id);
        List<OutletResponseDto> outletsDto = outlets
                .stream()
                .map(OutletMappers::convert)
                .toList();
        return new ResponseEntity<>(outletsDto, HttpStatus.OK);
    }
}
