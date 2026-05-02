package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CreateCourseUseCase createCourseUseCase;
    private final RelationshipAuthorizationService relationshipService;
    private final SubjectRepository subjectRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public Course createCourseInOutlet(Course course, String outletId,
                                       List<String> subjectIds, List<String> educatorIds, List<String> studentIds) {
        Course created = createCourseUseCase.createCourseInOutlet(course, outletId);
        String courseId = created.getId();
        String tenantId = created.getTenantId();

        relationshipService.writeRelationship("course", courseId, "parent_outlet", "outlet", outletId);

        subjectRepository.createSubjectCourseLinks(subjectIds, courseId, tenantId);

        if (!educatorIds.isEmpty()) {
            Map<String, String> educatorUserIds = educatorRepository.findUserIdsByEducatorIds(educatorIds);
            for (String educatorId : educatorIds) {
                educatorRepository.createEducatorCourseLink(educatorId, courseId, tenantId);
                String userId = educatorUserIds.get(educatorId);
                if (userId != null) {
                    relationshipService.writeRelationship("course", courseId, "educator", "user", userId);
                }
            }
        }

        if (!studentIds.isEmpty()) {
            Map<String, String> studentUserIds = studentRepository.findUserIdsByStudentIds(studentIds);
            for (String studentId : studentIds) {
                studentRepository.createStudentCourseLink(studentId, courseId, tenantId);
                String userId = studentUserIds.get(studentId);
                if (userId != null) {
                    relationshipService.writeRelationship("course", courseId, "viewer", "user", userId);
                }
            }
        }

        return created;
    }
}
