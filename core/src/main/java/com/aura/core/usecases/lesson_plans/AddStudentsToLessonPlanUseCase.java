package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.links.StudentLessonLink;
import java.util.List;

public interface AddStudentsToLessonPlanUseCase {
    List<StudentLessonLink> addStudentsToLessonPlan(String planId, List<String> studentIds, String tenantId);
}
