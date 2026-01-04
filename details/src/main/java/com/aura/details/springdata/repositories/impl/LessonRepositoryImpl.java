package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.details.springdata.mappers.lessons.LessonMapper;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import com.aura.details.springdata.tables.lessons.LessonTable;

public class LessonRepositoryImpl implements LessonRepository {
    private final JpaLessonRepository jpaLessonRepository;

    public LessonRepositoryImpl(JpaLessonRepository jpaLessonRepository) {
        this.jpaLessonRepository = jpaLessonRepository;
    }

    @Override
    public Lesson save(Lesson lesson) {
        LessonTable newLessonTable = LessonMapper.INSTANCE.toTableEntity(lesson);
        LessonTable createdLessonTable = jpaLessonRepository.save(newLessonTable);
        return LessonMapper.INSTANCE.toDomainEntity(createdLessonTable);
    }
}
