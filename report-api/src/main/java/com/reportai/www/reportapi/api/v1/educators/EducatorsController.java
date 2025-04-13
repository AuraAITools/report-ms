package com.reportai.www.reportapi.api.v1.educators;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Educator APIs", description = "APIs for educators of institutions")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class EducatorsController {

    private final OutletsService outletsService;

    private final EducatorsService educatorsService;

    private final TenantAwareAccountsService tenantAwareAccountsService;

    @Autowired
    public EducatorsController(OutletsService outletsService, EducatorsService educatorsService, TenantAwareAccountsService tenantAwareAccountsService) {
        this.outletsService = outletsService;
        this.educatorsService = educatorsService;
        this.tenantAwareAccountsService = tenantAwareAccountsService;
    }

    @Operation(summary = "creates a educator in a client account", description = "creates educator in an already created client account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/accounts/{account_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::accounts::educators:create'")
    @Transactional
    public ResponseEntity<EducatorResponseDTO> createEducatorInClientAccount(@PathVariable UUID id, @PathVariable("outlet_id") UUID outletId, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateEducatorRequestDTO createEducatorRequestDTO) {
        Educator newEducator = EducatorMappers.convert(createEducatorRequestDTO, id);
        Educator createdEducator = tenantAwareAccountsService.createEducatorInTenantAwareAccountInOutlet(accountId, outletId, createEducatorRequestDTO.getLevelIds(), createEducatorRequestDTO.getSubjectIds(), newEducator);
        return new ResponseEntity<>(EducatorMappers.convert(createdEducator), HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::educators:read'")
    @Transactional
    public ResponseEntity<List<EducatorResponseDTO>> getEducatorsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Educator> courses = outletsService.getOutletEducators(outletId);
        return new ResponseEntity<>(courses.stream().map(EducatorMappers::convert).toList(), HttpStatus.OK);
    }
}
