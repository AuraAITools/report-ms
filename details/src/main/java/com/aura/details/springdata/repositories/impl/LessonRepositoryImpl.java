package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.details.springdata.mappers.lessons.LessonMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonRepositoryImpl implements LessonRepository {
    private final JpaLessonRepository jpaLessonRepository;

    @Override
    public Lesson save(Lesson lesson) {
        LessonModel newLessonModel = LessonMapper.INSTANCE.toModelEntity(lesson);
        LessonModel createdLessonModel = jpaLessonRepository.save(newLessonModel);
        return LessonMapper.INSTANCE.toDomainEntity(createdLessonModel);
    }

    @Override
    public List<Lesson> updateLessons(List<Lesson> lessons) {
        List<LessonModel> lessonModels = lessons.stream().map(LessonMapper.INSTANCE::toModelEntity).toList();
        return jpaLessonRepository.saveAll(lessonModels).stream().map(LessonMapper.INSTANCE::toDomainEntity).toList();
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
