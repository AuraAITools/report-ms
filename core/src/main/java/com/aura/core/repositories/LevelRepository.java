package com.aura.core.repositories;

import com.aura.core.entities.levels.Level;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LevelRepository {
    PagedResult<Level> findAllBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);

    PagedResult<Level> findAllBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);

    @Nullable Level findByNameAndTenantId(String name, String tenantId);

    @Nullable Level findById(String id);

    List<Level> findAllByTenantId(String tenantId);

    List<Level> findAllByIds(List<UUID> levelIds);

    Map<String, List<Level>> findAllByLessonIds(List<String> lessonIds);

    Map<String, Level> findAllByCourseIds(List<String> courseIds);

    Map<String, List<Level>> findAllByEducatorIds(List<String> educatorIds);

    void deleteById(String id);

    void deleteAllLevelLessonLinksByLevelId(String levelId);

    Level save(Level level);
}
