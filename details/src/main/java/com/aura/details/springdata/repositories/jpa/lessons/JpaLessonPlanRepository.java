package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.tables.lessons.LessonPlanTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonPlanRepository extends JpaRepository<LessonPlanTable, UUID>, JpaSpecificationExecutor<LessonPlanTable> {
}

