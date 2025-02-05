package com.reportai.www.reportapi.api.v1.educators;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    public EducatorsController(OutletsService outletsService, EducatorsService educatorsService) {
        this.outletsService = outletsService;
        this.educatorsService = educatorsService;
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::educators:read'")
    @Transactional
    public ResponseEntity<List<CreateEducatorResponseDTO>> getEducatorsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        List<Educator> courses = outletsService.getOutletEducators(outletId);
        return new ResponseEntity<>(courses.stream().map(EducatorMappers::convert).toList(), HttpStatus.OK);
    }
}
