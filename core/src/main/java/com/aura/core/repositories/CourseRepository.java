package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface CourseRepository {
    Course save(Course course);

    @Nullable Course findById(String id);

    List<Course> findAllByOutletId(String outletId);

    List<Course> findAllByTenantId(String tenantId);

    List<Course> findAllByIds(List<String> courseIds);

    Map<String, List<Course>> findAllByStudentIds(List<String> studentIds);

    Map<String, List<Course>> findAllByEducatorIds(List<String> educatorIds);

    void deleteById(String id);

    void nullOutLevelForCourses(String levelId);
}
