package com.reportai.www.reportapi.api;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Autowired
    private InstitutionMaterialService institutionMaterialService;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private InstitutionMaterialController institutionMaterialController;

    @Autowired
    private ObjectMapper objectMapper;

    Topic topic;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        topic = Topic.builder().build();
    }

    @Test
    public void should_CreateMaterialForTopic_When_ValidRequest() throws Exception {
        Material material = Material.builder().name("test").topics(new HashSet<>()).build();
        topic = topicRepository.save(topic);
        mockMvc.perform(post("/api/v1/topics/{topic_id}/materials", topic.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(material)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(material.getName()));
    }

    @Test
    public void should_BatchCreateMaterialForTopic_When_ValidRequest() throws Exception {
        Material materialOne = Material.builder().name("test1").topics(new HashSet<>()).build();
        Material materialTwo = Material.builder().name("test2").topics(new HashSet<>()).build();
        List<Material> materials = Arrays.asList(materialOne, materialTwo);
        topic = topicRepository.save(topic);

        mockMvc.perform(post("/api/v1/topics/{topic_id}/materials/batch", topic.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materials)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").isNotEmpty())
            .andExpect(jsonPath("$[0].name").value(materialOne.getName()))
            .andExpect(jsonPath("$[1].id").isNotEmpty())
            .andExpect(jsonPath("$[1].name").value(materialTwo.getName()));
    }

    @Test
    public void should_GetAllMaterialsFromTopic_When_ValidRequest() throws Exception {
        Material materialOne = Material.builder().name("test1").topics(new HashSet<>()).build();
        Material materialTwo = Material.builder().name("test2").topics(new HashSet<>()).build();
        Set<Material> materials = new HashSet<>(Arrays.asList(materialOne, materialTwo));
        topic = topicRepository.save(topic);
        materials.forEach(material -> material.getTopics().add(topic));
        materialRepository.saveAll(materials);

        mockMvc.perform(get("/api/v1/topics/{topic_id}/materials", topic.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", containsInAnyOrder(materialOne.getName(), materialTwo.getName())));
    }

    @Test
    public void should_GetMaterialById_When_ValidRequest() throws Exception {
        Material material = Material.builder().build();
        Material savedMaterial = materialRepository.save(material);

        mockMvc.perform(get("/api/v1/materials/{material_id}", savedMaterial.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(savedMaterial.getName()));
    }

    @Test
    public void should_GetUpdatedMaterial_When_ValidRequest() throws Exception {
        Material material = Material.builder().topics(new HashSet<>()).build();
        material.getTopics().add(topic);
        Topic newTopic = Topic.builder().build();
        topicRepository.saveAll(List.of(topic, newTopic));
        Material savedMaterial = materialRepository.save(material);
        material.setName("material");
        material.getTopics().add(newTopic);

        mockMvc.perform(patch("/api/v1/materials/{material_id}", savedMaterial.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(material)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(material.getName()))
            .andExpect(jsonPath("$.topics", hasSize(2)));
    }
}
