package com.reportai.www.reportapi.api;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reportai.www.reportapi.api.v1.InstitutionMaterialController;
import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.repositories.MaterialRepository;
import com.reportai.www.reportapi.repositories.TopicRepository;
import com.reportai.www.reportapi.services.InstitutionMaterialService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionMaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InstitutionMaterialService institutionMaterialService;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private TopicRepository topicRepository;

    @InjectMocks
    private InstitutionMaterialController institutionMaterialController;

    @Autowired
    private ObjectMapper objectMapper;

    UUID topicId = UUID.randomUUID();
    UUID materialId = UUID.randomUUID();

    Topic topic;
    Material material;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        material = new Material();
        material.setId(materialId);
        when(materialRepository.findById(any(UUID.class))).thenReturn(Optional.of(material));

        topic = new Topic();
        topic.setId(topicId);
    }

    @Test
    public void should_CreateMaterialForTopic_When_ValidRequest() throws Exception {
        Material material = new Material();
        material.setId(materialId);
        topic.setMaterials(new ArrayList<>(List.of(material)));
        when(topicRepository.findById(any(UUID.class))).thenReturn(Optional.of(topic));

        when(institutionMaterialService.createMaterialForTopic(any(Material.class), any(UUID.class)))
            .thenReturn(material);
        String test = mockMvc.perform(post("/api/v1/institutions/{topic_id}/materials", topicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(material))).andReturn().getResponse().getContentAsString();
        System.out.println("aigsdioas");
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.id").value(material.getId().toString()))
//            .andExpect(jsonPath("$.name").value(material.getName()));
    }

    @Test
    public void should_BatchCreateMaterialForTopic_When_ValidRequest() throws Exception {
        Material material1 = new Material();
        material1.setId(UUID.randomUUID());
        material1.setName("Test Material 1");

        Material material2 = new Material();
        material2.setId(UUID.randomUUID());
        material2.setName("Test Material 2");

        List<Material> materials = Arrays.asList(material1, material2);

        when(institutionMaterialService.batchCreateMaterialForTopic(any(List.class), any(UUID.class)))
            .thenReturn(materials);

        mockMvc.perform(post("/api/v1/institutions/{topic_id}/materials/batch", topicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materials)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(material1.getId().toString()))
            .andExpect(jsonPath("$[0].name").value(material1.getName()))
            .andExpect(jsonPath("$[1].id").value(material2.getId().toString()))
            .andExpect(jsonPath("$[1].name").value(material2.getName()));
    }

    @Test
    public void should_GetAllMaterialsFromTopic_When_ValidRequest() throws Exception {
        Material material1 = new Material();
        material1.setId(UUID.randomUUID());
        material1.setName("Test Material 1");

        Material material2 = new Material();
        material2.setId(UUID.randomUUID());
        material2.setName("Test Material 2");

        List<Material> materials = Arrays.asList(material1, material2);

        topic.setMaterials(new ArrayList<>(materials));
        when(topicRepository.findById(any(UUID.class))).thenReturn(Optional.of(topic));
        when(institutionMaterialService.getAllMaterialsFromTopic(any(UUID.class))).thenReturn(materials);

        mockMvc.perform(get("/api/v1/institutions/{topic_id}/materials", topicId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(material1.getId().toString()))
            .andExpect(jsonPath("$[0].name").value(material1.getName()))
            .andExpect(jsonPath("$[1].id").value(material2.getId().toString()))
            .andExpect(jsonPath("$[1].name").value(material2.getName()));
    }

    @Test
    public void should_GetMaterialById_When_ValidRequest() throws Exception {
        when(institutionMaterialService.getMaterialFromTopic(any(UUID.class))).thenReturn(material);

        mockMvc.perform(get("/api/v1/institutions/materials/{material_id}", materialId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(material.getId().toString()))
            .andExpect(jsonPath("$.name").value(material.getName()));
    }
}
