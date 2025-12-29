package com.aura.details.springdata.repositories.jpa.courses;

import com.aura.details.springdata.tables.courses.CourseTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCourseRepository extends JpaRepository<CourseTable, UUID>, JpaSpecificationExecutor<CourseTable> {
}

