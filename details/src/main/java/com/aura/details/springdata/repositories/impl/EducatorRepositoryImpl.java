package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.EducatorCourseLink;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.EducatorRepository;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.mappers.links.EducatorCourseLinkMapper;
import com.aura.details.springdata.mappers.outlets.OutletMapper;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.EducatorCourseLinkModel;
import com.aura.details.springdata.models.links.LevelEducatorLinkModel;
import com.aura.details.springdata.models.links.OutletEducatorLinkModel;
import com.aura.details.springdata.models.links.SubjectEducatorLinkModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class EducatorRepositoryImpl implements EducatorRepository {
    private final JpaEducatorRepository jpaEducatorRepository;
    private final JpaCourseRepository jpaCourseRepository;
    private final JpaOutletRepository jpaOutletRepository;
    private final JpaLevelRepository jpaLevelRepository;
    private final JpaSubjectRepository jpaSubjectRepository;
    private final JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository;
    private final JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository;
    private final JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository;
    private final JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository;
    private final JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;
    private final JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository;

    @Override
    public @Nullable Educator findById(String id) {
        return jpaEducatorRepository.findById(UUID.fromString(id)).map(EducatorMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public Educator save(Educator educator) {
        EducatorModel educatorModel = EducatorMapper.INSTANCE.toModelEntity(educator);
        EducatorModel createdEducatorModel = jpaEducatorRepository.saveAndFlush(educatorModel);
        return EducatorMapper.INSTANCE.toDomainEntity(createdEducatorModel);
    }

    @Override
    public List<EducatorCourseLink> createEducatorCourseLinks(List<String> educatorIds, String courseId, String tenantId) {
        CourseModel courseModel = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        List<EducatorModel> educatorModels = jpaEducatorRepository.findAllById(educatorIds.stream().map(UUID::fromString).toList());
        if (educatorModels.size() != educatorIds.size()) {
            throw new TechnicalException("Could not find some educators with ids " + Arrays.toString(educatorIds.toArray()));
        }
        List<EducatorCourseLinkModel> educatorCourseLinkModels = educatorModels
                .stream()
                .<EducatorCourseLinkModel>map(educatorModel -> EducatorCourseLinkModel
                        .builder()
                        .course(courseModel)
                        .educator(educatorModel)
                        .tenantId(tenantId)
                        .build())
                .toList();
        return jpaEducatorCourseLinkRepository.saveAll(educatorCourseLinkModels)
                .stream()
                .map(EducatorCourseLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public Course createEducatorCourseLink(String educatorId, String courseId, String tenantId) {
        CourseModel courseModel = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        EducatorModel educatorModel = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        EducatorCourseLinkModel educatorCourseLinkModel = EducatorCourseLinkModel
                .builder()
                .course(courseModel)
                .educator(educatorModel)
                .tenantId(tenantId)
                .build();
        jpaEducatorCourseLinkRepository.save(educatorCourseLinkModel);
        return CourseMapper.INSTANCE.toDomainEntity(courseModel);
    }

    @Override
    public Outlet createOutletEducatorLink(String educatorId, String outletId, String tenantId) {
        OutletModel outletModel = jpaOutletRepository.getReferenceById(UUID.fromString(outletId));
        EducatorModel educatorModel = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        OutletEducatorLinkModel outletEducatorLinkModel = OutletEducatorLinkModel
                .builder()
                .outlet(outletModel)
                .educator(educatorModel)
                .tenantId(tenantId)
                .build();
        jpaOutletEducatorLinkRepository.save(outletEducatorLinkModel);
        return OutletMapper.INSTANCE.toDomainEntity(outletModel);
    }

    @Override
    public Level createLevelEducatorLink(String educatorId, String levelId, String tenantId) {
        LevelModel levelModel = jpaLevelRepository.getReferenceById(UUID.fromString(levelId));
        EducatorModel educatorModel = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        LevelEducatorLinkModel levelEducatorLinkModel = LevelEducatorLinkModel
                .builder()
                .level(levelModel)
                .educator(educatorModel)
                .tenantId(tenantId)
                .build();
        jpaLevelEducatorLinkRepository.save(levelEducatorLinkModel);
        return LevelMapper.INSTANCE.toDomainEntity(levelModel);
    }

    @Override
    public Subject createSubjectEducatorLink(String educatorId, String subjectId, String tenantId) {
        SubjectModel subjectModel = jpaSubjectRepository.getReferenceById(UUID.fromString(subjectId));
        EducatorModel educatorModel = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        SubjectEducatorLinkModel subjectEducatorLinkModel = SubjectEducatorLinkModel
                .builder()
                .subject(subjectModel)
                .educator(educatorModel)
                .tenantId(tenantId)
                .build();
        jpaSubjectEducatorLinkRepository.save(subjectEducatorLinkModel);
        return SubjectMapper.INSTANCE.toDomainEntity(subjectModel);
    }

    @Override
    public List<Educator> findAllByIds(List<UUID> educatorIds) {
        return jpaEducatorRepository.findAllById(educatorIds).stream().map(EducatorMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public Map<String, List<Educator>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<Educator>> result = new HashMap<>();
        for (Object[] row : jpaEducatorLessonLinkRepository.findEducatorsByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            EducatorModel educatorModel = (EducatorModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(EducatorMapper.INSTANCE.toDomainEntity(educatorModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Educator>> findAllByCourseIds(List<String> courseIds) {
        if (courseIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = courseIds.stream().map(UUID::fromString).toList();
        Map<String, List<Educator>> result = new HashMap<>();
        for (Object[] row : jpaEducatorCourseLinkRepository.findEducatorsByCourseIds(uuids)) {
            String courseId = row[0].toString();
            EducatorModel educatorModel = (EducatorModel) row[1];
            result.computeIfAbsent(courseId, k -> new ArrayList<>())
                    .add(EducatorMapper.INSTANCE.toDomainEntity(educatorModel));
        }
        return result;
    }

    @Override
    public Map<String, String> findUserIdsByEducatorIds(List<String> educatorIds) {
        if (educatorIds.isEmpty()) return Map.of();

        List<UUID> uuids = educatorIds.stream().map(UUID::fromString).toList();
        List<Object[]> results = jpaEducatorRepository.findUserIdsByIds(uuids);

        Map<String, String> map = new HashMap<>();
        for (Object[] row : results) {
            map.put(row[0].toString(), (String) row[1]);
        }
        return map;
    }

    @Override
    public void deleteAllEducatorCourseLinksByCourseId(String courseId) {
        jpaEducatorCourseLinkRepository.deleteAllByCourseId(UUID.fromString(courseId));
    }

    @Override
    public List<Educator> findAllByTenantId(String tenantId) {
        return jpaEducatorRepository.findByTenantId(tenantId).stream()
                .map(EducatorMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaEducatorRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllEducatorLessonLinksByEducatorId(String educatorId) {
        jpaEducatorLessonLinkRepository.deleteAllByEducatorId(UUID.fromString(educatorId));
    }

    @Override
    public void deleteAllLevelEducatorLinksByEducatorId(String educatorId) {
        jpaLevelEducatorLinkRepository.deleteAllByEducatorId(UUID.fromString(educatorId));
    }

    @Override
    public void deleteAllOutletEducatorLinksByEducatorId(String educatorId) {
        jpaOutletEducatorLinkRepository.deleteAllByEducatorId(UUID.fromString(educatorId));
    }

    @Override
    public void deleteAllSubjectEducatorLinksByEducatorId(String educatorId) {
        jpaSubjectEducatorLinkRepository.deleteAllByEducatorId(UUID.fromString(educatorId));
    }

    @Override
    public void deleteAllAccountEducatorLinksByEducatorId(String educatorId) {
        jpaAccountEducatorLinkRepository.deleteAllByEducatorId(UUID.fromString(educatorId));
    }

    @Override
    public void deleteAllEducatorCourseLinksByEducatorId(String educatorId) {
        jpaEducatorCourseLinkRepository.deleteAllByEducatorId(UUID.fromString(educatorId));
    }

    @Override
    public void updateProfileImageKeyById(String id, @Nullable String profileImageKey) {
        jpaEducatorRepository.updateProfileImageKeyById(UUID.fromString(id), profileImageKey);
    }
}
