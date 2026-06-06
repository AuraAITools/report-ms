package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.CourseRepository;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {
    private final JpaCourseRepository jpaCourseRepository;
    private final JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository;
    private final JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository;
    private final EntityManager entityManager;

    @Override
    public Course save(Course course) {
        CourseModel model = CourseMapper.INSTANCE.toModelEntity(course);
        model.setOutlet(Refs.ref(entityManager, OutletModel.class, course.getOutletId()));
        model.setLevel(Refs.ref(entityManager, LevelModel.class, course.getLevelId()));
        CourseModel newCourse = jpaCourseRepository.save(model);
        return CourseMapper.INSTANCE.toDomainEntity(newCourse);
    }

    @Override
    public @Nullable Course findById(String id) {
        Optional<CourseModel> course = jpaCourseRepository.findById(UUID.fromString(id));
        return course.map(CourseMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<Course> findAllByOutletId(String outletId) {
        return jpaCourseRepository.findByOutlet_Id(UUID.fromString(outletId)).stream()
                .map(CourseMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Course> findAllByTenantId(String tenantId) {
        return jpaCourseRepository.findByTenantId(tenantId).stream()
                .map(CourseMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Course> findAllByIds(List<String> courseIds) {
        if (courseIds.isEmpty()) return List.of();
        List<UUID> uuids = courseIds.stream().map(UUID::fromString).toList();
        return jpaCourseRepository.findAllById(uuids).stream()
                .map(CourseMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<Course>> findAllByStudentIds(List<String> studentIds) {
        if (studentIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = studentIds.stream().map(UUID::fromString).toList();
        Map<String, List<Course>> result = new HashMap<>();
        for (Object[] row : jpaStudentCourseLinkRepository.findCoursesByStudentIds(uuids)) {
            String studentId = row[0].toString();
            CourseModel courseModel = (CourseModel) row[1];
            result.computeIfAbsent(studentId, k -> new ArrayList<>())
                    .add(CourseMapper.INSTANCE.toDomainEntity(courseModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Course>> findAllByEducatorIds(List<String> educatorIds) {
        if (educatorIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = educatorIds.stream().map(UUID::fromString).toList();
        Map<String, List<Course>> result = new HashMap<>();
        for (Object[] row : jpaEducatorCourseLinkRepository.findCoursesByEducatorIds(uuids)) {
            String educatorId = row[0].toString();
            CourseModel courseModel = (CourseModel) row[1];
            result.computeIfAbsent(educatorId, k -> new ArrayList<>())
                    .add(CourseMapper.INSTANCE.toDomainEntity(courseModel));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaCourseRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void nullOutLevelForCourses(String levelId) {
        jpaCourseRepository.nullOutLevelForCourses(UUID.fromString(levelId));
    }
}
