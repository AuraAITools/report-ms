package com.reportai.www.reportapi.utils;

import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.views.LessonView;
import com.reportai.www.reportapi.repositories.views.LessonViewRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class LessonViewProjectionDecorator implements ViewProjectionDecorator<Lesson, LessonView> {

    private final LessonViewRepository lessonViewRepository;

    @Autowired
    public LessonViewProjectionDecorator(LessonViewRepository lessonViewRepository) {
        this.lessonViewRepository = lessonViewRepository;
    }

    @Override
    public JpaRepository<LessonView, UUID> getViewRepository() {
        return lessonViewRepository;
    }

}
