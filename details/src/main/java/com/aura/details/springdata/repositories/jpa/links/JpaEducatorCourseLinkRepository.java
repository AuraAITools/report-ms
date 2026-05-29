package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.EducatorCourseLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEducatorCourseLinkRepository extends JpaRepository<EducatorCourseLinkModel, UUID>, JpaSpecificationExecutor<EducatorCourseLinkModel> {

    @Query("SELECT l.course.id, l.educator FROM EducatorCourseLinkModel l WHERE l.course.id IN :courseIds")
    List<Object[]> findEducatorsByCourseIds(@Param("courseIds") List<UUID> courseIds);

    @Query("SELECT l.educator.id, l.course FROM EducatorCourseLinkModel l WHERE l.educator.id IN :educatorIds")
    List<Object[]> findCoursesByEducatorIds(@Param("educatorIds") List<UUID> educatorIds);

    void deleteAllByCourseId(UUID courseId);

    void deleteAllByEducatorId(UUID educatorId);
}
