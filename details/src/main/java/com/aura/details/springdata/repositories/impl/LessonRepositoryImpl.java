package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.details.springdata.mappers.lessons.LessonMapper;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import com.aura.details.springdata.tables.lessons.LessonTable;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonRepositoryImpl implements LessonRepository {
    private final JpaLessonRepository jpaLessonRepository;

    @Override
    public Lesson save(Lesson lesson) {
        LessonTable newLessonTable = LessonMapper.INSTANCE.toTableEntity(lesson);
        LessonTable createdLessonTable = jpaLessonRepository.save(newLessonTable);
        return LessonMapper.INSTANCE.toDomainEntity(createdLessonTable);
    }

    @Override
    public List<Lesson> updateLessons(List<Lesson> lessons) {
        List<LessonTable> lessonTables = lessons.stream().map(LessonMapper.INSTANCE::toTableEntity).toList();
        return jpaLessonRepository.saveAll(lessonTables).stream().map(LessonMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public @Nullable Lesson findById(UUID id) {
        return jpaLessonRepository.findById(id)
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<Lesson> findAllById(List<UUID> ids) {
        return jpaLessonRepository.findAllById(ids).stream().map(LessonMapper.INSTANCE::toDomainEntity).toList();
    }
}
