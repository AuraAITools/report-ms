package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.StudentLessonReportRepository;
import com.aura.details.springdata.mappers.links.StudentLessonReportMapper;
import com.aura.details.springdata.models.lessons.LessonTopicProficiencyModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonReportRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentLessonReportRepositoryImpl implements StudentLessonReportRepository {
    private final JpaStudentLessonReportRepository jpaStudentLessonReportRepository;
    private final EntityManager entityManager;

    @Override
    public StudentLessonReport save(StudentLessonReport report) {
        StudentLessonReportModel model = StudentLessonReportMapper.INSTANCE.toModelEntity(report);
        model.setStudentLessonLink(Refs.ref(entityManager, StudentLessonLinkModel.class, report.getStudentLessonLinkId()));

        if (model.getLessonHomeworkCompletion() != null) {
            model.getLessonHomeworkCompletion().setStudentLessonReport(model);
        }
        if (model.getLessonParticipationReview() != null) {
            model.getLessonParticipationReview().setStudentLessonReport(model);
        }
        if (model.getLessonTopicProficiencies() != null) {
            for (LessonTopicProficiencyModel p : model.getLessonTopicProficiencies()) {
                p.setStudentLessonReport(model);
                if (p.getTopic() != null && p.getTopic().getId() != null) {
                    p.setTopic(Refs.ref(entityManager, TopicModel.class, p.getTopic().getId().toString()));
                }
            }
        }

        StudentLessonReportModel saved = jpaStudentLessonReportRepository.save(model);
        return StudentLessonReportMapper.INSTANCE.toDomainEntity(saved);
    }
}
