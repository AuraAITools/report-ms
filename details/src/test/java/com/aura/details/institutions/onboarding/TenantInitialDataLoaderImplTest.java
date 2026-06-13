package com.aura.details.institutions.onboarding;

import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.levels.LevelTemplateModel;
import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.models.schools.SchoolTemplateModel;
import com.aura.details.springdata.models.subjects.SubjectLevelLinkTemplateModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.subjects.SubjectTemplateModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.models.topics.TopicSubjectLinkTemplateModel;
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

    @InjectMocks
    private TenantInitialDataLoaderImpl loader;

    private static final String SYSTEM_USER_ID = "00000000-0000-0000-0000-000000000000";

    @DisplayName("Should mark every seeded entity and link as authored by the system user")
    @Test
    void loadInitialData_setsSystemUserAsCreatedByAndUpdatedBy() {
        String tenantId = "tenant-456";

        SchoolTemplateModel schoolTemplate = new SchoolTemplateModel();
        schoolTemplate.setName("Primary School");
        schoolTemplate.setSchoolCategory(SchoolCategory.PRIMARY);
        when(jpaSchoolTemplateRepository.findAll()).thenReturn(List.of(schoolTemplate));

        LevelTemplateModel levelTemplate = new LevelTemplateModel();
        levelTemplate.setName("Primary 1");
        levelTemplate.setSchoolCategory(SchoolCategory.PRIMARY);
        when(jpaLevelTemplateRepository.findAll()).thenReturn(List.of(levelTemplate));

        SubjectTemplateModel subjectTemplate = new SubjectTemplateModel();
        subjectTemplate.setName("Math");
        when(jpaSubjectTemplateRepository.findAll()).thenReturn(List.of(subjectTemplate));

        TopicTemplateModel topicTemplate = new TopicTemplateModel();
        topicTemplate.setName("Algebra");
        when(jpaTopicTemplateRepository.findAll()).thenReturn(List.of(topicTemplate));

        SubjectLevelLinkTemplateModel subjectLevelLinkTemplate = SubjectLevelLinkTemplateModel
                .builder()
                .subjectName("Math")
                .levelName("Primary 1")
                .build();
        when(jpaSubjectLevelLinkTemplateRepository.findAll()).thenReturn(List.of(subjectLevelLinkTemplate));

        TopicSubjectLinkTemplateModel topicSubjectLinkTemplate = TopicSubjectLinkTemplateModel
                .builder()
                .topicName("Algebra")
                .subjectName("Math")
                .build();
        when(jpaTopicSubjectLinkTemplateRepository.findAll()).thenReturn(List.of(topicSubjectLinkTemplate));

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

        ArgumentCaptor<List<SchoolModel>> schoolsCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaSchoolRepository).saveAll(schoolsCaptor.capture());
        assertThat(schoolsCaptor.getValue())
                .isNotEmpty()
                .allSatisfy(school -> {
                    assertThat(school.getCreatedBy()).isEqualTo(SYSTEM_USER_ID);
                    assertThat(school.getUpdatedBy()).isEqualTo(SYSTEM_USER_ID);
                });

        ArgumentCaptor<List<LevelModel>> levelsCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaLevelRepository).saveAll(levelsCaptor.capture());
        assertThat(levelsCaptor.getValue())
                .isNotEmpty()
                .allSatisfy(level -> {
                    assertThat(level.getCreatedBy()).isEqualTo(SYSTEM_USER_ID);
                    assertThat(level.getUpdatedBy()).isEqualTo(SYSTEM_USER_ID);
                });

        ArgumentCaptor<List<SubjectModel>> subjectsCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaSubjectRepository).saveAll(subjectsCaptor.capture());
        assertThat(subjectsCaptor.getValue())
                .isNotEmpty()
                .allSatisfy(subject -> {
                    assertThat(subject.getCreatedBy()).isEqualTo(SYSTEM_USER_ID);
                    assertThat(subject.getUpdatedBy()).isEqualTo(SYSTEM_USER_ID);
                });

        ArgumentCaptor<List<TopicModel>> topicsCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaTopicRepository).saveAll(topicsCaptor.capture());
        assertThat(topicsCaptor.getValue())
                .isNotEmpty()
                .allSatisfy(topic -> {
                    assertThat(topic.getCreatedBy()).isEqualTo(SYSTEM_USER_ID);
                    assertThat(topic.getUpdatedBy()).isEqualTo(SYSTEM_USER_ID);
                });

        ArgumentCaptor<List<SubjectLevelLinkModel>> subjectLevelLinksCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaSubjectLevelLinkRepository).saveAll(subjectLevelLinksCaptor.capture());
        assertThat(subjectLevelLinksCaptor.getValue())
                .isNotEmpty()
                .allSatisfy(link -> {
                    assertThat(link.getCreatedBy()).isEqualTo(SYSTEM_USER_ID);
                    assertThat(link.getUpdatedBy()).isEqualTo(SYSTEM_USER_ID);
                });

        ArgumentCaptor<List<TopicSubjectLinkModel>> topicSubjectLinksCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaTopicSubjectLinkRepository).saveAll(topicSubjectLinksCaptor.capture());
        assertThat(topicSubjectLinksCaptor.getValue())
                .isNotEmpty()
                .allSatisfy(link -> {
                    assertThat(link.getCreatedBy()).isEqualTo(SYSTEM_USER_ID);
                    assertThat(link.getUpdatedBy()).isEqualTo(SYSTEM_USER_ID);
                });
    }
}
