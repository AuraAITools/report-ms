package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.StudentCourseLink;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface StudentRepository {
    Student save(Student student);

    @Nullable Student findById(String id);

    List<Student> findAllByIds(List<UUID> studentIds);

    List<Student> saveAll(List<Student> students);

    Level tagStudentToLevel(String studentId, String levelId, String tenantId);

    School tagStudentToSchool(String studentId, String schoolId, String tenantId);

    Course createStudentCourseLink(String studentId, String courseId, String tenantId);

    List<StudentCourseLink> createStudentCourseLinks(List<String> studentId, String courseId, String tenantId);

    Map<String, String> findUserIdsByStudentIds(List<String> studentIds);

    Map<String, List<Student>> findAllByLessonIds(List<String> lessonIds);

    Map<String, List<Student>> findAllByCourseIds(List<String> courseIds);

    void deleteAllStudentCourseLinksByCourseId(String courseId);

    List<Student> findAllByTenantId(String tenantId);

    void deleteById(String id);

    // Link-cleanup methods (used by StudentService.deleteById)
    void deleteAllStudentCourseLinksByStudentId(String studentId);

    void deleteAllStudentOutletLinksByStudentId(String studentId);

    void deleteAllStudentLessonLinksByStudentId(String studentId);

    void deleteAllSubjectStudentLinksByStudentId(String studentId);

    void deleteAllAccountStudentLinksByStudentId(String studentId);

    // Batch lookups for @BatchMapping on StudentResponse
    Map<String, Level> findLevelsByStudentIds(List<String> studentIds);

    Map<String, School> findSchoolsByStudentIds(List<String> studentIds);
}
