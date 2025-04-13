package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
}
