package com.aura.core.repositories;

import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface SubjectRepository {
    Subject save(Subject subject);

    @Nullable Subject findById(String id);

    List<Subject> findAllByIds(List<UUID> subjectIds);

    List<Subject> findAllByTenantId(String tenantId);

    SubjectWithTopics link(UUID subjectId, List<UUID> topicIds, String tenantId);

    List<SubjectCourseLink> createSubjectCourseLinks(List<String> subjectIds, String courseId, String tenantId);

    Map<String, List<Subject>> findAllByLessonIds(List<String> lessonIds);

    Map<String, List<Subject>> findAllByCourseIds(List<String> courseIds);

    Map<String, List<Subject>> findAllByEducatorIds(List<String> educatorIds);

    Map<String, List<Topic>> findTopicsBySubjectIds(List<String> subjectIds);

    void deleteById(String id);

    void deleteAllTopicSubjectLinksBySubjectId(String subjectId);

    void deleteAllSubjectCourseLinksBySubjectId(String subjectId);

    void deleteAllSubjectCourseLinksByCourseId(String courseId);
}
