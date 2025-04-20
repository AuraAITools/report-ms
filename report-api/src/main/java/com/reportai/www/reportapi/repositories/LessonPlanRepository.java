package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.lessons.LessonPlan;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonPlanRepository extends JpaRepository<LessonPlan, UUID> {
}