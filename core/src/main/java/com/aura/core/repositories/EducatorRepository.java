package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.subjects.Subject;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface EducatorRepository {

    @Nullable Educator findById(String id);

    Educator save(Educator educator);

    Course createEducatorCourseLink(String educatorId, String courseId, String tenantId);

    Outlet createOutletEducatorLink(String educatorId, String outletId, String tenantId);

    Level createLevelEducatorLink(String educatorId, String levelId, String tenantId);

    Subject createSubjectEducatorLink(String educatorId, String subjectId, String tenantId);

    List<Educator> findByIds(List<UUID> educatorIds);
}
