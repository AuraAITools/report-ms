package com.aura.details.springdata.repositories.jpa.courses;

import com.aura.details.springdata.models.courses.CourseModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    List<CourseModel> findByOutlet_Id(UUID outletId);

    @Query("SELECT c.id, c.level FROM CourseModel c WHERE c.id IN :courseIds AND c.level IS NOT NULL")
    List<Object[]> findLevelsByCourseIds(@Param("courseIds") List<UUID> courseIds);

    @Modifying
    @Query("UPDATE CourseModel c SET c.level = null WHERE c.level.id = :levelId")
    void nullOutLevelForCourses(@Param("levelId") UUID levelId);
}

