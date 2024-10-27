package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.dtos.requests.OnboardInstitutionDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.mappers.InstitutionMappers;
import com.reportai.www.reportapi.services.AuraAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Aura Report Admin APIs",
        description = "APIs for administrators and development team of Aura Report"
)
@RestController
@RequestMapping("/api/v1/aura-admin")
@Validated
@Slf4j
public class AuraAdminController {


    private final AuraAdminService auraAdminService;

    @Autowired
    public AuraAdminController(AuraAdminService auraAdminService) {
        this.auraAdminService = auraAdminService;
    }

    @Operation(
            summary = "onboards an institution",
            description = "onboards an institution. create an account for an new institution. this request also requires a initial institution admin user to be specified"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "created"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "existing institution account already exists"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "unexpected internal server error has occured"
            )
    })
    @PostMapping("/institutions")
    public ResponseEntity<Void> onboardInstitution(@RequestBody @Valid OnboardInstitutionDTO onboardInstitutionDTO) {
        Institution institution = InstitutionMappers.convert(onboardInstitutionDTO.getInstitution());
        Account adminUser = AccountMappers.convert(onboardInstitutionDTO.getAdminAccount());

        Account onboardedAccount = auraAdminService.onboardInstitution(institution, adminUser);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("user_id", onboardedAccount.getUserId());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }
}
