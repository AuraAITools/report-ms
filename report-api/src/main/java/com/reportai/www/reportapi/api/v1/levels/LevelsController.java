package com.reportai.www.reportapi.api.v1.levels;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.levels.requests.CreateLevelsDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.CreateLevelsResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.mappers.LevelMappers;
import com.reportai.www.reportapi.services.levels.LevelsService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.reportai.www.reportapi.mappers.LevelMappers.convert;

@Tag(name = "Levels APIs", description = "APIs for managing a Levels resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class LevelsController {

    private final LevelsService levelsService;

    @Autowired
    public LevelsController(LevelsService levelsService) {
        this.levelsService = levelsService;
    }

    @Operation(summary = "create a level for a institution", description = "create an level for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/levels")
    @HasResourcePermission(permission = "'institutions::' + #id + '::levels:create'")
    public ResponseEntity<CreateLevelsResponseDTO> createLevelForInstitution(@PathVariable UUID id, @Valid @RequestBody CreateLevelsDTO createLevelsDTO) {
        Level createdLevel = levelsService.createLevelForInstitution(id, convert(id, createLevelsDTO));
        return new ResponseEntity<>(convert(createdLevel), HttpStatus.OK);
    }

    @Operation(summary = "get all levels for a institution", description = "get all levels for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/levels")
    @HasResourcePermission(permission = "'institutions::' + #id + '::levels:read'")
    public ResponseEntity<List<CreateLevelsResponseDTO>> getAllLevelsForInstitution(@PathVariable UUID id) {
        List<Level> levels = levelsService.getAllLevelsForInstitution(id);
        List<CreateLevelsResponseDTO> levelsDTO = levels
                .stream()
                .map(LevelMappers::convert)
                .toList();
        return new ResponseEntity<>(levelsDTO, HttpStatus.OK);
    }
}
