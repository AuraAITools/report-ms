package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.EducatorCourseLink;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.subjects.Subject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface EducatorRepository {

    @Nullable Educator findById(String id);

    Educator save(Educator educator);

    Course createEducatorCourseLink(String educatorId, String courseId, String tenantId);

    List<EducatorCourseLink> createEducatorCourseLinks(List<String> educatorId, String courseId, String tenantId);

    Outlet createOutletEducatorLink(String educatorId, String outletId, String tenantId);

    Level createLevelEducatorLink(String educatorId, String levelId, String tenantId);

    Subject createSubjectEducatorLink(String educatorId, String subjectId, String tenantId);

    List<Educator> findAllByIds(List<UUID> educatorIds);

    Map<String, String> findUserIdsByEducatorIds(List<String> educatorIds);

    Map<String, List<Educator>> findAllByLessonIds(List<String> lessonIds);

    Map<String, List<Educator>> findAllByCourseIds(List<String> courseIds);

    void deleteAllEducatorCourseLinksByCourseId(String courseId);

    List<Educator> findAllByTenantId(String tenantId);

    void deleteById(String id);

    void deleteAllEducatorLessonLinksByEducatorId(String educatorId);

    void deleteAllLevelEducatorLinksByEducatorId(String educatorId);

    void deleteAllOutletEducatorLinksByEducatorId(String educatorId);

    void deleteAllSubjectEducatorLinksByEducatorId(String educatorId);

    void deleteAllAccountEducatorLinksByEducatorId(String educatorId);

    void deleteAllEducatorCourseLinksByEducatorId(String educatorId);

    void updateProfileImageKeyById(String id, @Nullable String profileImageKey);
}
