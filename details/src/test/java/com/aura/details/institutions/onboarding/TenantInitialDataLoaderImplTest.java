package com.aura.details.institutions.onboarding;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.levels.LevelTemplateModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.subjects.SubjectTemplateModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.models.topics.TopicTemplateModel;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelTemplateRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLevelLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectLevelLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicSubjectLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicTemplateRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tenant initial data loader")
@ExtendWith(MockitoExtension.class)
class TenantInitialDataLoaderImplTest {

    @Mock
    private JpaSchoolTemplateRepository jpaSchoolTemplateRepository;
    @Mock
    private JpaSchoolRepository jpaSchoolRepository;
    @Mock
    private JpaLevelTemplateRepository jpaLevelTemplateRepository;
    @Mock
    private JpaLevelRepository jpaLevelRepository;
    @Mock
    private JpaSubjectTemplateRepository jpaSubjectTemplateRepository;
    @Mock
    private JpaSubjectRepository jpaSubjectRepository;
    @Mock
    private JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository;
    @Mock
    private JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository;
    @Mock
    private JpaTopicTemplateRepository jpaTopicTemplateRepository;
    @Mock
    private JpaTopicRepository jpaTopicRepository;
    @Mock
    private JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository;
    @Mock
    private JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    @Mock
    private RelationshipAuthorizationService relationshipService;

    @InjectMocks
    private TenantInitialDataLoaderImpl loader;

    @DisplayName("Should write parent_institution relationships for levels, subjects, and topics")
    @Test
    void loadInitialData_writesRelationshipsForAllResources() {
        String tenantId = "tenant-123";

        LevelTemplateModel levelTemplate = new LevelTemplateModel();
        levelTemplate.setName("Primary 1");
        when(jpaLevelTemplateRepository.findAll()).thenReturn(List.of(levelTemplate));

        SubjectTemplateModel subjectTemplate = new SubjectTemplateModel();
        subjectTemplate.setName("Math");
        when(jpaSubjectTemplateRepository.findAll()).thenReturn(List.of(subjectTemplate));

        TopicTemplateModel topicTemplate = new TopicTemplateModel();
        topicTemplate.setName("Algebra");
        when(jpaTopicTemplateRepository.findAll()).thenReturn(List.of(topicTemplate));

        // Return empty lists for templates that require lookups
        when(jpaSchoolTemplateRepository.findAll()).thenReturn(List.of());
        when(jpaSubjectLevelLinkTemplateRepository.findAll()).thenReturn(List.of());
        when(jpaTopicSubjectLinkTemplateRepository.findAll()).thenReturn(List.of());

        // Mock saveAll to return the input with IDs set
        when(jpaLevelRepository.saveAll(anyList())).thenAnswer(inv -> {
            List<LevelModel> models = inv.getArgument(0);
            models.forEach(m -> m.setId(UUID.randomUUID()));
            return models;
        });
        when(jpaSubjectRepository.saveAll(anyList())).thenAnswer(inv -> {
            List<SubjectModel> models = inv.getArgument(0);
            models.forEach(m -> m.setId(UUID.randomUUID()));
            return models;
        });
        when(jpaTopicRepository.saveAll(anyList())).thenAnswer(inv -> {
            List<TopicModel> models = inv.getArgument(0);
            models.forEach(m -> m.setId(UUID.randomUUID()));
            return models;
        });

        loader.loadInitialDataForTenant(tenantId);

        // Verify relationships written for each resource type
        ArgumentCaptor<String> resourceTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resourceIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(relationshipService, atLeastOnce()).writeRelationship(
                resourceTypeCaptor.capture(), resourceIdCaptor.capture(),
                eq("parent_institution"),
                eq("institution"),
                eq(tenantId));

        assertThat(resourceTypeCaptor.getAllValues()).contains("level", "subject", "topic");
    }
}
