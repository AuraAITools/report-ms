package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.LessonObjective;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectiveRepository extends JpaRepository<LessonObjective, UUID> {
}
