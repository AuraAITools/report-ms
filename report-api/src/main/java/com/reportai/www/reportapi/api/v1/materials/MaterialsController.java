package com.reportai.www.reportapi.api.v1.materials;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.materials.requests.CreateMaterialRequestDTO;
import com.reportai.www.reportapi.api.v1.materials.response.MaterialResponseDTO;
import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.services.materials.MaterialsService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: @Mingde
 */
@Tag(name = "Material APIs", description = "APIs for managing a Material resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class MaterialsController {
    private final ModelMapper modelMapper = new ModelMapper();

    private final MaterialsService materialsService;

    @Autowired
    public MaterialsController(MaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    /**
     * TODO: add material permissions into aura token for admin
     *
     * @param material
     * @param id
     * @return
     */
    @PostMapping("/institutions/{id}/materials")
    @HasResourcePermission(permission = "'institutions::' + #id + '::materials:create'")
    public ResponseEntity<MaterialResponseDTO> createMaterialInInstitution(@RequestBody CreateMaterialRequestDTO material, @PathVariable UUID id) {
        Material newMaterial = new Material();
        modelMapper.map(material, newMaterial);
        Material createdMaterial = materialsService.create(newMaterial, material.getFile());
        MaterialResponseDTO materialResponseDTO = new MaterialResponseDTO();
        modelMapper.map(createdMaterial, materialResponseDTO);
        return new ResponseEntity<>(materialResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{id}/materials")
    @HasResourcePermission(permission = "'institutions::' + #id + '::materials:read'")
    public ResponseEntity<List<Material>> getAllMaterialsInInstitution(@PathVariable UUID id) {
        Collection<Material> materials = materialsService.getAll();
        return new ResponseEntity<>(materials.stream().toList(), HttpStatus.OK);
    }

    @PatchMapping("/institutions/{id}/subjects/{topic_id}/materials/{material_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::subjects::materials:attach'")
    public ResponseEntity<Void> linkMaterialToSubject(@PathVariable UUID id, @PathVariable(name = "topic_id") UUID topicId, @PathVariable(name = "material_id") UUID materialId) {
        materialsService.attachMaterialToTopic(topicId, materialId);
        return ResponseEntity.ok().build();
    }
}
