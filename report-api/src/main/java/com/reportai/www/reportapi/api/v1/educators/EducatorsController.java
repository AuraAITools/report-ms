package com.reportai.www.reportapi.api.v1.educators;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.educators.requests.CreateEducatorRequestDTO;
import com.reportai.www.reportapi.entities.educators.Educator;
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
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    @Autowired
    public EducatorsController(OutletsService outletsService, EducatorsService educatorsService, ModelMapper modelMapper) {
        this.outletsService = outletsService;
        this.educatorsService = educatorsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "creates a educator", description = "creates educator in an institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/educators")
    @Transactional
    public ResponseEntity<EducatorResponseDTO> createEducator(@PathVariable UUID id, @RequestBody @Valid CreateEducatorRequestDTO createEducatorRequestDTO) {
        Educator educator = educatorsService.create(createEducatorRequestDTO);
        return new ResponseEntity<>(modelMapper.map(educator, EducatorResponseDTO.class), HttpStatus.OK);
    }


    @GetMapping("/institutions/{id}/outlets/{outlet_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::educators:read'")
    @Transactional
    public ResponseEntity<List<EducatorResponseDTO>> getEducatorsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Educator> educators = outletsService.getOutletEducators(outletId);
        List<EducatorResponseDTO> educatorResponseDTOS = educators.stream().map(educator -> modelMapper.map(educator, EducatorResponseDTO.class)).toList();
        return new ResponseEntity<>(educatorResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/educators/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::educators:read'")
    @Transactional
    public ResponseEntity<List<ExpandedEducatorResponseDTO>> getExpandedEducatorsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Educator> educators = outletsService.getOutletEducators(outletId);
        List<ExpandedEducatorResponseDTO> educatorResponseDTOS = educators.stream().map(educator -> modelMapper.map(educator, ExpandedEducatorResponseDTO.class)).toList();
        return new ResponseEntity<>(educatorResponseDTOS, HttpStatus.OK);
    }


    @GetMapping("/institutions/{id}/educators")
    @Transactional// TODO: implement perms
    public ResponseEntity<List<EducatorResponseDTO>> getEducatorsFromOutlet(@PathVariable UUID id) {
        List<EducatorResponseDTO> educatorResponseDTOS = educatorsService.findAll().stream().map(educator -> modelMapper.map(educator, EducatorResponseDTO.class)).toList();
        return new ResponseEntity<>(educatorResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/educators/expand")
    @Transactional// TODO: implement perms
    public ResponseEntity<List<ExpandedEducatorResponseDTO>> getExpandedEducatorsFromOutlet(@PathVariable UUID id) {
        List<ExpandedEducatorResponseDTO> educatorResponseDTOS = educatorsService.findAll().stream().map(educator -> modelMapper.map(educator, ExpandedEducatorResponseDTO.class)).toList();
        return new ResponseEntity<>(educatorResponseDTOS, HttpStatus.OK);
    }
}
