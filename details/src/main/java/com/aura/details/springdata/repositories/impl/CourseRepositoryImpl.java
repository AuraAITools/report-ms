package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.CourseRepository;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.tables.courses.CourseTable;
import java.util.Optional;
import java.util.UUID;

public class CourseRepositoryImpl implements CourseRepository {
    private final JpaCourseRepository jpaCourseRepository;

    public CourseRepositoryImpl(JpaCourseRepository jpaCourseRepository) {
        this.jpaCourseRepository = jpaCourseRepository;
    }

    @Override
    public Course save(Course course) {
        CourseTable courseToBeCreated = CourseMapper.INSTANCE.toTableEntity(course);
        CourseTable newCourse = jpaCourseRepository.save(courseToBeCreated);
        return CourseMapper.INSTANCE.toDomainEntity(newCourse);
    }

    @Override
    public Course findById(String id) {
        Optional<CourseTable> course = jpaCourseRepository.findById(UUID.fromString(id));
        return course.map(CourseMapper.INSTANCE::toDomainEntity).orElse(null);
    }
}
