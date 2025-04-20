package com.reportai.www.reportapi.repositories.views;

import com.reportai.www.reportapi.entities.views.LessonView;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonViewRepository extends JpaRepository<LessonView, UUID> {

    public List<LessonView> findAllByOutletId(UUID outletId);
}