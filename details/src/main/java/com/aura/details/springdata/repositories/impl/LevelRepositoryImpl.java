package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PageInfo;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.aura.core.utils.SortDirection;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelLessonLinkRepository;
import com.aura.details.springdata.specifications.LevelSpecification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;


import static com.aura.details.springdata.specifications.TenantSpecification.whereTenantId;

public class LevelRepositoryImpl implements LevelRepository {
    JpaLevelRepository jpaLevelRepository;
    JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository;
    JpaCourseRepository jpaCourseRepository;
    JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository;

    public LevelRepositoryImpl(JpaLevelRepository jpaLevelRepository,
                               JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository,
                               JpaCourseRepository jpaCourseRepository,
                               JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository) {
        this.jpaLevelRepository = jpaLevelRepository;
        this.jpaLevelLessonLinkRepository = jpaLevelLessonLinkRepository;
        this.jpaCourseRepository = jpaCourseRepository;
        this.jpaLevelEducatorLinkRepository = jpaLevelEducatorLinkRepository;
    }

    @Override
    public PagedResult<Level> findAllBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());

        Specification<LevelModel> searchSpecifications = LevelSpecification.buildLevelSearchSpecification(searchCriteria);

        Window<LevelModel> windowResults = jpaLevelRepository.findBy(
                searchSpecifications.and(whereTenantId(tenantId)),
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(LevelMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext()));
    }

    @Override
    public PagedResult<Level> findAllBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());

        Specification<LevelModel> searchSpecifications = LevelSpecification.buildLevelSearchSpecification(searchCriteria);

        Window<LevelModel> windowResults = jpaLevelRepository.findBy(
                searchSpecifications,
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(LevelMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext()));
    }

    @Override
    public @Nullable Level findByNameAndTenantId(String name, String tenantId) {
        LevelModel levelModel = jpaLevelRepository.findByNameAndTenantId(name, tenantId);
        return LevelMapper.INSTANCE.toDomainEntity(levelModel);
    }

    @Override
    public @Nullable Level findById(String id) {
        return jpaLevelRepository.findById(UUID.fromString(id)).map(LevelMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<Level> findAllByTenantId(String tenantId) {
        return jpaLevelRepository.findAll(whereTenantId(tenantId)).stream()
                .map(LevelMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Level> findAllByIds(List<UUID> levelIds) {
        return jpaLevelRepository.findAllById(levelIds).stream().map(LevelMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public Map<String, List<Level>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<Level>> result = new HashMap<>();
        for (Object[] row : jpaLevelLessonLinkRepository.findLevelsByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            LevelModel levelModel = (LevelModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(LevelMapper.INSTANCE.toDomainEntity(levelModel));
        }
        return result;
    }

    @Override
    public Map<String, Level> findAllByCourseIds(List<String> courseIds) {
        if (courseIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = courseIds.stream().map(UUID::fromString).toList();
        Map<String, Level> result = new HashMap<>();
        for (Object[] row : jpaCourseRepository.findLevelsByCourseIds(uuids)) {
            String courseId = row[0].toString();
            LevelModel levelModel = (LevelModel) row[1];
            result.put(courseId, LevelMapper.INSTANCE.toDomainEntity(levelModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Level>> findAllByEducatorIds(List<String> educatorIds) {
        if (educatorIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = educatorIds.stream().map(UUID::fromString).toList();
        Map<String, List<Level>> result = new HashMap<>();
        for (Object[] row : jpaLevelEducatorLinkRepository.findLevelsByEducatorIds(uuids)) {
            String educatorId = row[0].toString();
            LevelModel levelModel = (LevelModel) row[1];
            result.computeIfAbsent(educatorId, k -> new ArrayList<>())
                    .add(LevelMapper.INSTANCE.toDomainEntity(levelModel));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaLevelRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllLevelLessonLinksByLevelId(String levelId) {
        jpaLevelLessonLinkRepository.deleteAllByLevelId(UUID.fromString(levelId));
    }

    @Override
    public Level save(Level level) {
        LevelModel model = LevelMapper.INSTANCE.toModelEntity(level);
        LevelModel saved = jpaLevelRepository.save(model);
        return LevelMapper.INSTANCE.toDomainEntity(saved);
    }
}
