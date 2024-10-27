package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.dtos.requests.CreateClientAccountDTO;
import com.reportai.www.reportapi.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Institution Admin APIs",
        description = "APIs for administrators of institutions"
)
@RestController
@RequestMapping("/api/v1")
@Validated
public class InstitutionAdminController {


    private final AdminService adminService;

    @Autowired
    public InstitutionAdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @Operation(
            summary = "create an account for the clients of a institution",
            description = "create an account for the clients of a institution. A client of a institution is defined as the person who is registering with the institution, it could be either a student or a parent. The client will be able to log in to the Aura Report mobile app after email verification"
    )
    @ApiResponse(
            responseCode = "201",
            description = "created"
    )
    @PostMapping("/institutions/{id}/accounts")
    public ResponseEntity<Void> createClientAccount(@PathVariable UUID id, @RequestBody @Valid CreateClientAccountDTO createClientAccountDTO) {
        String createdUserId = adminService.createStudentAccountForInstitution(createClientAccountDTO, id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("user_id", createdUserId);
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

}
