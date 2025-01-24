package com.reportai.www.reportapi.api.v1.outlets;

import com.reportai.www.reportapi.api.v1.outlets.requests.CreateOutletDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDto;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.mappers.OutletMappers;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Tag(name = "Outlet APIs", description = "APIs for managing a Outlet resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class OutletsController {

    private final OutletsService outletsService;

    @Autowired
    public OutletsController(OutletsService outletsService) {
        this.outletsService = outletsService;
    }

    @Operation(summary = "create a outlet for a institution", description = "create an outlet for a institution")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @PostMapping("/institutions/{id}/outlets")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<OutletResponseDto> createOutletForInstitution(@PathVariable UUID id, @Valid @RequestBody CreateOutletDTO createOutletDTO) {
        Outlet newOutlet = OutletMappers.convert(createOutletDTO, id.toString());
        Outlet createdOutlet = outletsService.createOutletForInstitution(id, newOutlet);
        OutletResponseDto outletResponseDto = OutletMappers.convert(createdOutlet);
        return new ResponseEntity<>(outletResponseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "get all outlets for a institution", description = "get all outlets for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<List<OutletResponseDto>> getOutletsForInstitution(@PathVariable UUID id) {
        List<Outlet> outlets = outletsService.getAllOutletsForInstitution(id);
        List<OutletResponseDto> outletsDto = outlets
                .stream()
                .map(OutletMappers::convert)
                .toList();
        return new ResponseEntity<>(outletsDto, HttpStatus.OK);
    }
}
