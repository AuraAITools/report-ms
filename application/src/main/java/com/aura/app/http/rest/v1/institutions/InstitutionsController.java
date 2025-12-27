package com.aura.app.http.rest.v1.institutions;


import com.aura.app.http.rest.v1.institutions.mappers.AppInstitutionMapper;
import com.aura.app.http.rest.v1.institutions.mappers.UserMapper;
import com.aura.app.http.rest.v1.institutions.requests.CreateInstitutionRequestDTO;
import com.aura.app.http.rest.v1.institutions.responses.InstitutionResponseDTO;
import com.aura.core.auth.AuthorizationValidator;
import com.aura.core.auth.RBACAuthorizationStrategy;
import com.aura.core.entities.accounts.TenantAwareRole;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.institutions.services.OnboardingInstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
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

    private static final Set<TenantAwareRole> ALLOWED_ROLES = Set.of(TenantAwareRole
            .builder()
            .name("aura-admin")
            .roleType(TenantAwareRole.ROLE_TYPE.TECHNICAL_USER)
            .build());
    public final AuthorizationValidator roleValidator;
    private final OnboardingInstitutionService onboardingInstitutionService;


    public InstitutionsController(AuthorizationValidator roleValidator, OnboardingInstitutionService onboardingInstitutionService) {
        this.roleValidator = roleValidator;
        this.onboardingInstitutionService = onboardingInstitutionService;
    }

    @Operation(summary = "creates an institution", description = "creates an institution with default roles. Please create an admin account on the institution to continue")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions")
    @Transactional
    public ResponseEntity<InstitutionResponseDTO> createInstitution(@RequestBody @Valid CreateInstitutionRequestDTO createInstitutionRequestDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Jwt jwt)) throw new RuntimeException("Invalid principal type");
        roleValidator.validateAuthorization(new RBACAuthorizationStrategy(ALLOWED_ROLES, UserMapper.from(jwt)));
        Institution institution = AppInstitutionMapper.INSTANCE.from(createInstitutionRequestDTO);
        onboardingInstitutionService.onboard(institution);
        return new ResponseEntity<>(AppInstitutionMapper.INSTANCE.from(institution), HttpStatus.OK);
    }
}
