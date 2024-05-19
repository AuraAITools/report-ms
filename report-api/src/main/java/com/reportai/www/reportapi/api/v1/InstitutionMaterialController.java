package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.services.InstitutionMaterialService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/institutions")
@RestController
public class InstitutionMaterialController {

    private final InstitutionMaterialService institutionMaterialService;

    public InstitutionMaterialController(InstitutionMaterialService institutionMaterialService) {
        this.institutionMaterialService = institutionMaterialService;
    }

    @PostMapping("/{topic_id}/materials")
    public ResponseEntity<Material> createMaterialForTopic(@RequestBody Material material,
        @PathVariable(name = "topic_id") UUID topicId) {
        Material createdMaterial = institutionMaterialService.createMaterialForTopic(material,
            topicId);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @PostMapping("/{topic_id}/materials/batch")
    public ResponseEntity<List<Material>> batchCreateMaterialForTopic(@RequestBody List<Material> material,
        @PathVariable(name = "topic_id") UUID topicId) {
        List<Material> createdMaterials = institutionMaterialService.batchCreateMaterialForTopic(
            material, topicId);
        return new ResponseEntity<>(createdMaterials, HttpStatus.CREATED);
    }

    @GetMapping("/{topic_id}/materials")
    public ResponseEntity<List<Material>> getAllMaterialsFromTopic(@PathVariable(name = "topic_id") UUID topicId) {
        List<Material> materials = institutionMaterialService.getAllMaterialsFromTopic(topicId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/materials/{material_id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable(name = "material_id") UUID materialId) {
        Material material = institutionMaterialService.getMaterialFromTopic(materialId);
        return new ResponseEntity<>(material, HttpStatus.OK);
    }
}
