package com.reportai.www.reportapi.repositories.views;

import com.reportai.www.reportapi.entities.lessons.LessonView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonViewRepository extends JpaRepository<LessonView, Long> {
    public Optional<LessonView> findById(UUID lessonViewId);

    public List<LessonView> findAllByOutletId(UUID outletId);
}