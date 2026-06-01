package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.usecases.courses.CourseQueryUseCase;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import com.aura.core.usecases.courses.DeleteCourseUseCase;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CreateCourseUseCase createCourseUseCase;
    private final CourseQueryUseCase courseQueryUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;
    private final LessonService lessonService;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final RelationshipAuthorizationService relationshipService;
    private final SubjectRepository subjectRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;

    public @Nullable Course findById(String id) {
        return courseQueryUseCase.findById(id);
    }

    public List<Course> findAllByOutletId(String outletId) {
        return courseQueryUseCase.findAllByOutletId(outletId);
    }

    public List<Course> findAllByEducatorId(String educatorId) {
        return courseQueryUseCase.findAllByEducatorId(educatorId);
    }

    public List<Course> findAllByStudentId(String studentId) {
        return courseQueryUseCase.findAllByStudentId(studentId);
    }

    @Transactional
    public Course createCourseInOutlet(Course course, String outletId,
                                       List<String> subjectIds, List<String> educatorIds, List<String> studentIds) {
        Course created = createCourseUseCase.createCourseInOutlet(course, outletId);
        String courseId = created.getId();
        String tenantId = created.getTenantId();
        subjectRepository.createSubjectCourseLinks(subjectIds, courseId, tenantId);


        List<RelationshipAuthorizationService.Relationship> relationships = new ArrayList<>();
        relationships.add(new RelationshipAuthorizationService.Relationship(
                "course", courseId, "user_group", "course_user_group", courseId));
        relationships.add(new RelationshipAuthorizationService.Relationship(
                "course_user_group", courseId, "parent", "system_user_group", "aura"));
        relationships.add(new RelationshipAuthorizationService.Relationship(
                "course", courseId, "parent", "outlet", outletId));

        if (!educatorIds.isEmpty()) {
            educatorRepository.createEducatorCourseLinks(educatorIds, courseId, tenantId);
            Map<String, String> educatorUserIds = educatorRepository.findUserIdsByEducatorIds(educatorIds);
            for (String userId : educatorUserIds.values()) {
                if (userId == null) continue;
                relationships.add(new RelationshipAuthorizationService.Relationship(
                        "course_user_group", courseId, "modifier", "user", userId));
            }
        }

        if (!studentIds.isEmpty()) {
            Map<String, String> studentUserIds = studentRepository.findUserIdsByStudentIds(studentIds);
            studentRepository.createStudentCourseLinks(studentIds, courseId, tenantId);
            for (String userId : studentUserIds.values()) {
                if (userId == null) continue;
                relationships.add(new RelationshipAuthorizationService.Relationship(
                        "course_user_group", courseId, "viewer", "user", userId));
            }
        }

        relationshipService.writeRelationships(relationships);
        return created;
    }

    @Transactional
    public void deleteById(String id) {
        for (Lesson lesson : lessonRepository.findAllByCourseId(id)) {
            lessonService.deleteById(lesson.getId());
        }
        subjectRepository.deleteAllSubjectCourseLinksByCourseId(id);
        educatorRepository.deleteAllEducatorCourseLinksByCourseId(id);
        studentRepository.deleteAllStudentCourseLinksByCourseId(id);
        deleteCourseUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("course", id);
        relationshipService.deleteAllRelationshipsForResource("course_user_group", id);
    }
}
