package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Lesson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonRepository extends JpaRepository<Lesson, UUID>, JpaSpecificationExecutor<Lesson> {
    List<Lesson> findAllByOutletId(UUID outletId);

    List<Lesson> findAllByInstitutionId(UUID outletId);

}
