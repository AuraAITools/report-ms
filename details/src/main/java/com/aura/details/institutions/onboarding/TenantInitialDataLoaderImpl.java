package com.aura.details.institutions.onboarding;

import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
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
import com.aura.details.springdata.tables.levels.LevelTable;
import com.aura.details.springdata.tables.levels.LevelTemplateTable;
import com.aura.details.springdata.tables.links.SubjectLevelLinkTable;
import com.aura.details.springdata.tables.links.TopicSubjectLinkTable;
import com.aura.details.springdata.tables.schools.SchoolTable;
import com.aura.details.springdata.tables.schools.SchoolTemplateTable;
import com.aura.details.springdata.tables.subjects.SubjectLevelLinkTemplateTable;
import com.aura.details.springdata.tables.subjects.SubjectTable;
import com.aura.details.springdata.tables.subjects.SubjectTemplateTable;
import com.aura.details.springdata.tables.topics.TopicSubjectLinkTemplateTable;
import com.aura.details.springdata.tables.topics.TopicTable;
import com.aura.details.springdata.tables.topics.TopicTemplateTable;
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

    private static SubjectTable mapToSubject(SubjectTemplateTable subjectTemplateTable, String tenantId) {
        return SubjectTable
                .builder()
                .name(subjectTemplateTable.getName())
                .tenantId(tenantId)
                .build();
    }

    private static LevelTable mapToLevelTable(LevelTemplateTable levelTemplateTable, String tenantId) {
        return LevelTable
                .builder()
                .name(levelTemplateTable.getName())
                .tenantId(tenantId)
                .category(levelTemplateTable.getSchoolCategory())
                .build();
    }

    private static SchoolTable mapToSchoolTable(SchoolTemplateTable schoolTemplateTable, String tenantId) {
        return SchoolTable
                .builder()
                .name(schoolTemplateTable.getName())
                .tenantId(tenantId)
                .schoolCategory(schoolTemplateTable.getSchoolCategory())
                .build();
    }

    private static TopicTable mapToTopic(TopicTemplateTable topicTemplateTable, String tenantId) {
        return TopicTable
                .builder()
                .name(topicTemplateTable.getName())
                .tenantId(tenantId)
                .build();
    }

    @Transactional
    @Override
    public void loadInitialDataForTenant(String tenantId) {
// INSERT default schools from school_templates
        List<SchoolTemplateTable> defaultSchoolTemplates = jpaSchoolTemplateRepository.findAll();
        List<SchoolTable> defaultSchoolTables = defaultSchoolTemplates
                .stream()
                .map(dst -> mapToSchoolTable(dst, tenantId))
                .toList();
        jpaSchoolRepository.saveAll(defaultSchoolTables);

        // INSERT default levels from level_templates
        List<LevelTemplateTable> levelTemplateTables = jpaLevelTemplateRepository.findAll();
        List<LevelTable> levelTables = levelTemplateTables.stream().map(ltt -> mapToLevelTable(ltt, tenantId)).toList();
        jpaLevelRepository.saveAll(levelTables);

        // insert default subjects from subject templates
        List<SubjectTemplateTable> subjectTemplateTables = jpaSubjectTemplateRepository.findAll();
        List<SubjectTable> subjects = subjectTemplateTables.stream().map(stt -> mapToSubject(stt, tenantId)).toList();
        jpaSubjectRepository.saveAll(subjects);

        // insert default topics
        List<TopicTemplateTable> topicTemplateTables = jpaTopicTemplateRepository.findAll();
        List<TopicTable> topics = topicTemplateTables.stream().map(ttt -> mapToTopic(ttt, tenantId)).toList();
        jpaTopicRepository.saveAll(topics);

        // ✅ PRE-LOAD all levels and subjects into Maps
        Map<String, LevelTable> levelsByName = levelTables.stream()
                .collect(Collectors.toMap(LevelTable::getName, Function.identity()));
        Map<String, SubjectTable> subjectsByName = subjects.stream()
                .collect(Collectors.toMap(SubjectTable::getName, Function.identity()));
        Map<String, TopicTable> topicsByName = topics.stream()
                .collect(Collectors.toMap(TopicTable::getName, Function.identity()));

        // insert default subject-level links
        List<SubjectLevelLinkTemplateTable> subjectLevelLinkTemplateTables = jpaSubjectLevelLinkTemplateRepository.findAll();
        List<SubjectLevelLinkTable> subjectLevelLinks = subjectLevelLinkTemplateTables
                .stream()
                .flatMap(template -> mapToSubjectLevelLink(template, tenantId, levelsByName, subjectsByName))
                .toList();
        jpaSubjectLevelLinkRepository.saveAll(subjectLevelLinks);

        // insert default topic-subject links
        List<TopicSubjectLinkTemplateTable> topicSubjectLinkTemplateTables = jpaTopicSubjectLinkTemplateRepository.findAll();
        List<TopicSubjectLinkTable> topicSubjectLinkTables = topicSubjectLinkTemplateTables
                .stream()
                .flatMap(template -> mapToTopicSubjectLink(template, tenantId, topicsByName, subjectsByName))
                .toList();
        jpaTopicSubjectLinkRepository.saveAll(topicSubjectLinkTables);
    }

    private Stream<TopicSubjectLinkTable> mapToTopicSubjectLink(
            TopicSubjectLinkTemplateTable template,
            String tenantId,
            Map<String, TopicTable> topicsByName,
            Map<String, SubjectTable> subjectsByName) {

        SubjectTable subject = subjectsByName.get(template.getSubjectName());
        TopicTable topic = topicsByName.get(template.getTopicName());

        if (subject != null && topic != null) {
            return Stream.of(TopicSubjectLinkTable
                    .builder()
                    .subject(subject)
                    .topic(topic)
                    .tenantId(tenantId)
                    .build());
        }
        return Stream.empty();
    }

    private Stream<SubjectLevelLinkTable> mapToSubjectLevelLink(
            SubjectLevelLinkTemplateTable template,
            String tenantId,
            Map<String, LevelTable> levelsByName,
            Map<String, SubjectTable> subjectsByName) {

        LevelTable level = levelsByName.get(template.getLevelName());
        SubjectTable subject = subjectsByName.get(template.getSubjectName());

        if (level != null && subject != null) {
            return Stream.of(SubjectLevelLinkTable
                    .builder()
                    .level(level)
                    .subject(subject)
                    .tenantId(tenantId)
                    .build());
        }
        return Stream.empty();
    }
}
