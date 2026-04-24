package com.aura.details.institutions.onboarding;

import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
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
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

// TODO: abstract repositories and move to core
@RequiredArgsConstructor
public class TenantInitialDataLoaderImpl implements TenantInitialDataLoader {

    private final JpaSchoolTemplateRepository jpaSchoolTemplateRepository;
    private final JpaSchoolRepository jpaSchoolRepository;
    private final JpaLevelTemplateRepository jpaLevelTemplateRepository;
    private final JpaLevelRepository jpaLevelRepository;
    private final JpaSubjectTemplateRepository jpaSubjectTemplateRepository;
    private final JpaSubjectRepository jpaSubjectRepository;
    private final JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository;
    private final JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository;
    private final JpaTopicTemplateRepository jpaTopicTemplateRepository;
    private final JpaTopicRepository jpaTopicRepository;
    private final JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository;
    private final JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;

    @Transactional
    @Override
    public void loadInitialDataForTenant(String tenantId) {
// INSERT default schools from school_templates
        List<SchoolTemplateModel> defaultSchoolTemplates = jpaSchoolTemplateRepository.findAll();
        List<SchoolModel> defaultSchoolModels = defaultSchoolTemplates
                .stream()
                .map(dst -> mapToSchoolModel(dst, tenantId))
                .toList();
        jpaSchoolRepository.saveAll(defaultSchoolModels);

        // INSERT default levels from level_templates
        List<LevelTemplateModel> levelTemplateModels = jpaLevelTemplateRepository.findAll();
        List<LevelModel> levelModels = levelTemplateModels.stream().map(ltt -> mapToLevelModel(ltt, tenantId)).toList();
        jpaLevelRepository.saveAll(levelModels);

        // insert default subjects from subject templates
        List<SubjectTemplateModel> subjectTemplateModels = jpaSubjectTemplateRepository.findAll();
        List<SubjectModel> subjects = subjectTemplateModels.stream().map(stt -> mapToSubject(stt, tenantId)).toList();
        jpaSubjectRepository.saveAll(subjects);

        // insert default topics
        List<TopicTemplateModel> topicTemplateModels = jpaTopicTemplateRepository.findAll();
        List<TopicModel> topics = topicTemplateModels.stream().map(ttt -> mapToTopic(ttt, tenantId)).toList();
        jpaTopicRepository.saveAll(topics);

        // ✅ PRE-LOAD all levels and subjects into Maps
        Map<String, LevelModel> levelsByName = levelModels.stream()
                .collect(Collectors.toMap(LevelModel::getName, Function.identity()));
        Map<String, SubjectModel> subjectsByName = subjects.stream()
                .collect(Collectors.toMap(SubjectModel::getName, Function.identity()));
        Map<String, TopicModel> topicsByName = topics.stream()
                .collect(Collectors.toMap(TopicModel::getName, Function.identity()));

        // insert default subject-level links
        List<SubjectLevelLinkTemplateModel> subjectLevelLinkTemplateModels = jpaSubjectLevelLinkTemplateRepository.findAll();
        List<SubjectLevelLinkModel> subjectLevelLinks = subjectLevelLinkTemplateModels
                .stream()
                .flatMap(template -> mapToSubjectLevelLink(template, tenantId, levelsByName, subjectsByName))
                .toList();
        jpaSubjectLevelLinkRepository.saveAll(subjectLevelLinks);

        // insert default topic-subject links
        List<TopicSubjectLinkTemplateModel> topicSubjectLinkTemplateModels = jpaTopicSubjectLinkTemplateRepository.findAll();
        List<TopicSubjectLinkModel> topicSubjectLinkModels = topicSubjectLinkTemplateModels
                .stream()
                .flatMap(template -> mapToTopicSubjectLink(template, tenantId, topicsByName, subjectsByName))
                .toList();
        jpaTopicSubjectLinkRepository.saveAll(topicSubjectLinkModels);
    }

    private static SubjectModel mapToSubject(SubjectTemplateModel subjectTemplateModel, String tenantId) {
        return SubjectModel
                .builder()
                .name(subjectTemplateModel.getName())
                .tenantId(tenantId)
                .build();
    }

    private static LevelModel mapToLevelModel(LevelTemplateModel levelTemplateModel, String tenantId) {
        return LevelModel
                .builder()
                .name(levelTemplateModel.getName())
                .tenantId(tenantId)
                .category(levelTemplateModel.getSchoolCategory())
                .build();
    }

    private static SchoolModel mapToSchoolModel(SchoolTemplateModel schoolTemplateModel, String tenantId) {
        return SchoolModel
                .builder()
                .name(schoolTemplateModel.getName())
                .tenantId(tenantId)
                .schoolCategory(schoolTemplateModel.getSchoolCategory())
                .build();
    }

    private static TopicModel mapToTopic(TopicTemplateModel topicTemplateModel, String tenantId) {
        return TopicModel
                .builder()
                .name(topicTemplateModel.getName())
                .tenantId(tenantId)
                .build();
    }

    private Stream<TopicSubjectLinkModel> mapToTopicSubjectLink(
            TopicSubjectLinkTemplateModel template,
            String tenantId,
            Map<String, TopicModel> topicsByName,
            Map<String, SubjectModel> subjectsByName) {

        SubjectModel subject = subjectsByName.get(template.getSubjectName());
        TopicModel topic = topicsByName.get(template.getTopicName());

        if (subject != null && topic != null) {
            return Stream.of(TopicSubjectLinkModel
                    .builder()
                    .subject(subject)
                    .topic(topic)
                    .tenantId(tenantId)
                    .build());
        }
        return Stream.empty();
    }

    private Stream<SubjectLevelLinkModel> mapToSubjectLevelLink(
            SubjectLevelLinkTemplateModel template,
            String tenantId,
            Map<String, LevelModel> levelsByName,
            Map<String, SubjectModel> subjectsByName) {

        LevelModel level = levelsByName.get(template.getLevelName());
        SubjectModel subject = subjectsByName.get(template.getSubjectName());

        if (level != null && subject != null) {
            return Stream.of(SubjectLevelLinkModel
                    .builder()
                    .level(level)
                    .subject(subject)
                    .tenantId(tenantId)
                    .build());
        }
        return Stream.empty();
    }
}
