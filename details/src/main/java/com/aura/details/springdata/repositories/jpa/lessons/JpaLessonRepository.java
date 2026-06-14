package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonModel;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonRepository extends JpaRepository<LessonModel, UUID>, JpaSpecificationExecutor<LessonModel> {

    List<LessonModel> findByCourse_IdIn(Collection<UUID> courseIds);

    List<LessonModel> findByOutlet_Id(UUID outletId);

    List<LessonModel> findByTenantId(String tenantId);

    @Query("""
            SELECT l FROM LessonModel l
            WHERE l.tenantId = :tenantId
              AND l.outlet.id = :outletId
              AND l.state <> com.aura.details.springdata.models.lessons.LessonModel.LESSON_STATE.CANCELLED
              AND l.lessonStartTimestamptz < :endExclusive
              AND l.lessonEndTimestamptz > :startInclusive
            ORDER BY l.lessonStartTimestamptz ASC
            """)
    List<LessonModel> findAllByOutletIdAndTimeRange(@Param("tenantId") String tenantId,
                                                    @Param("outletId") UUID outletId,
                                                    @Param("startInclusive") Instant startInclusive,
                                                    @Param("endExclusive") Instant endExclusive);
}
