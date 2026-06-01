package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.details.springdata.mappers.links.StudentLessonLinkMapper;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentLessonLinkRepositoryImpl implements StudentLessonLinkRepository {
    private final JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public StudentLessonLink save(StudentLessonLink studentLessonLink) {
        StudentLessonLinkModel model = toAttachedModel(studentLessonLink);
        StudentLessonLinkModel saved = jpaStudentLessonLinkRepository.save(model);
        return StudentLessonLinkMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public List<StudentLessonLink> saveAll(List<StudentLessonLink> studentLessonLinks) {
        var modelEntities = studentLessonLinks.stream().map(this::toAttachedModel).toList();
        var createdModelEntities = jpaStudentLessonLinkRepository.saveAll(modelEntities);
        return createdModelEntities.stream().map(StudentLessonLinkMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public List<StudentLessonLink> findAllByLessonIdAndStudentIds(String lessonId, List<String> studentIds) {
        if (studentIds.isEmpty()) return List.of();
        List<StudentLessonLinkModel> models = jpaStudentLessonLinkRepository.findAllByLessonIdAndStudentIdIn(
                UUID.fromString(lessonId),
                studentIds.stream().map(UUID::fromString).toList());
        return models.stream().map(StudentLessonLinkMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public List<StudentLessonLink> assignLessonGroupToStudentLessonLinks(String lessonId, List<String> studentIds, String lessonGroupId) {
        if (studentIds.isEmpty()) return List.of();
        List<StudentLessonLinkModel> models = jpaStudentLessonLinkRepository.findAllByLessonIdAndStudentIdIn(
                UUID.fromString(lessonId),
                studentIds.stream().map(UUID::fromString).toList());
        LessonGroupModel groupRef = Refs.ref(entityManager, LessonGroupModel.class, lessonGroupId);
        for (StudentLessonLinkModel m : models) m.setLessonGroup(groupRef);
        return models.stream().map(StudentLessonLinkMapper.INSTANCE::toDomainEntity).toList();
    }

    private StudentLessonLinkModel toAttachedModel(StudentLessonLink link) {
        StudentLessonLinkModel model = StudentLessonLinkMapper.INSTANCE.toModelEntity(link);
        model.setStudent(Refs.ref(entityManager, StudentModel.class, link.getStudentId()));
        model.setLesson(Refs.ref(entityManager, LessonModel.class, link.getLessonId()));
        return model;
    }
}
