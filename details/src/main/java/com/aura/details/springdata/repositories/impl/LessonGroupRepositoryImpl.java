package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.details.springdata.mappers.lessons.LessonGroupMapper;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonGroupRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonObjectiveRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonQuizRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonQuizStudentLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanMaterialsLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonObjectiveTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import static java.util.Collections.emptyMap;

@RequiredArgsConstructor
public class LessonGroupRepositoryImpl implements LessonGroupRepository {
    private final JpaLessonGroupRepository jpaLessonGroupRepository;
    private final JpaLessonObjectiveTopicLinkRepository jpaLessonObjectiveTopicLinkRepository;
    private final JpaLessonQuizStudentLessonLinkRepository jpaLessonQuizStudentLessonLinkRepository;
    private final JpaLessonObjectiveRepository jpaLessonObjectiveRepository;
    private final JpaLessonQuizRepository jpaLessonQuizRepository;
    private final JpaLessonGroupPlanMaterialsLinkRepository jpaLessonGroupPlanMaterialsLinkRepository;
    private final JpaLessonGroupPlanTopicLinkRepository jpaLessonGroupPlanTopicLinkRepository;
    private final JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public LessonGroup save(LessonGroup lessonGroup) {
        LessonGroupModel model = LessonGroupMapper.INSTANCE.toModelEntity(lessonGroup);
        model.setLesson(Refs.ref(entityManager, LessonModel.class, lessonGroup.getLessonId()));
        LessonGroupModel saved = jpaLessonGroupRepository.save(model);
        return LessonGroupMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonGroup findById(UUID id) {
        return jpaLessonGroupRepository.findById(id)
                .map(LessonGroupMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<LessonGroup> findAllByLessonId(String lessonId) {
        return jpaLessonGroupRepository.findAllByLessonId(UUID.fromString(lessonId)).stream()
                .map(LessonGroupMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<LessonGroup> findAllByIds(List<UUID> ids) {
        return jpaLessonGroupRepository.findAllById(ids).stream()
                .map(LessonGroupMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<LessonGroup>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<LessonGroup>> result = new HashMap<>();
        for (LessonGroupModel model : jpaLessonGroupRepository.findAllByLessonIdIn(uuids)) {
            String lessonId = model.getLesson().getId().toString();
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(LessonGroupMapper.INSTANCE.toDomainEntity(model));
        }
        return result;
    }

    @Override
    public void deleteById(UUID id) {
        jpaLessonGroupRepository.deleteById(id);
    }

    @Override
    public void deleteAllLessonGroupChildrenByLessonGroupId(String lessonGroupId) {
        UUID groupUuid = UUID.fromString(lessonGroupId);
        jpaLessonObjectiveTopicLinkRepository.deleteAllByLessonObjective_LessonGroupPlan_LessonGroup_Id(groupUuid);
        jpaLessonQuizStudentLessonLinkRepository.deleteAllByLessonQuizTable_LessonGroupPlan_LessonGroup_Id(groupUuid);
        jpaLessonObjectiveRepository.deleteAllByLessonGroupPlan_LessonGroup_Id(groupUuid);
        jpaLessonQuizRepository.deleteAllByLessonGroupPlan_LessonGroup_Id(groupUuid);
        jpaLessonGroupPlanMaterialsLinkRepository.deleteAllByLessonGroupPlan_LessonGroup_Id(groupUuid);
        jpaLessonGroupPlanTopicLinkRepository.deleteAllByLessonGroupPlan_LessonGroup_Id(groupUuid);
        List<StudentLessonLinkModel> links = jpaStudentLessonLinkRepository.findAllByLessonGroup_Id(groupUuid);
        links.forEach(link -> link.setLessonGroup(null));
        jpaStudentLessonLinkRepository.saveAll(links);
    }
}
