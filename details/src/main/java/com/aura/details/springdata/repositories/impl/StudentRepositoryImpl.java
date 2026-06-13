package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.StudentCourseLink;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.StudentRepository;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.mappers.links.StudentCourseLinkMapper;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import com.aura.details.springdata.mappers.students.StudentMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentOutletLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {
    private final JpaStudentRepository jpaStudentRepository;
    private final JpaLevelRepository jpaLevelRepository;
    private final JpaSchoolRepository jpaSchoolRepository;
    private final JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository;
    private final JpaCourseRepository jpaCourseRepository;
    private final JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository;
    private final JpaStudentOutletLinkRepository jpaStudentOutletLinkRepository;
    private final JpaSubjectStudentLinkRepository jpaSubjectStudentLinkRepository;
    private final JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository;
    private final EntityManager entityManager;

    @Override
    public Student save(Student student) {
        StudentModel model = toAttachedModel(student);
        StudentModel createdStudentModel = jpaStudentRepository.save(model);
        return StudentMapper.INSTANCE.toDomainEntity(createdStudentModel);
    }

    @Override
    public @Nullable Student findById(String id) {
        StudentModel studentModel = jpaStudentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("student", id));
        return StudentMapper.INSTANCE.toDomainEntity(studentModel);
    }

    @Override
    public void updateProfileImageKeyById(String id, @Nullable String profileImageKey) {
        jpaStudentRepository.updateProfileImageKeyById(UUID.fromString(id), profileImageKey);
    }

    @Override
    public List<Student> findAllByIds(List<UUID> studentIds) {
        return jpaStudentRepository.findAllById(studentIds).stream().map(StudentMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        List<StudentModel> studentModels = students.stream().map(this::toAttachedModel).toList();
        List<StudentModel> createdStudentModels = jpaStudentRepository.saveAll(studentModels);
        return createdStudentModels.stream().map(StudentMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public Level tagStudentToLevel(String studentId, String levelId, String tenantId) {
        LevelModel level = jpaLevelRepository.getReferenceById(UUID.fromString(levelId));
        StudentModel studentModel = jpaStudentRepository.getReferenceById(UUID.fromString(studentId));
        studentModel.setLevel(level);
        jpaStudentRepository.save(studentModel);
        return LevelMapper.INSTANCE.toDomainEntity(level);
    }

    @Override
    public School tagStudentToSchool(String studentId, String schoolId, String tenantId) {
        SchoolModel school = jpaSchoolRepository.getReferenceById(UUID.fromString(schoolId));
        StudentModel studentModel = jpaStudentRepository.getReferenceById(UUID.fromString(studentId));
        studentModel.setSchool(school);
        jpaStudentRepository.save(studentModel);
        return SchoolMapper.INSTANCE.toDomainEntity(school);
    }

    @Override
    public List<StudentCourseLink> createStudentCourseLinks(List<String> studentIds, String courseId, String tenantId) {
        CourseModel courseModel = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        List<StudentModel> studentModels = jpaStudentRepository.findAllById(studentIds.stream().map(UUID::fromString).toList());
        if (studentModels.size() != studentIds.size()) {
            throw new TechnicalException("Could not find some students with ids " + Arrays.toString(studentIds.toArray()));
        }
        List<StudentCourseLinkModel> studentCourseLinkModels = studentModels
                .stream()
                .<StudentCourseLinkModel>map(studentModel -> StudentCourseLinkModel
                        .builder()
                        .course(courseModel)
                        .student(studentModel)
                        .tenantId(tenantId)
                        .build())
                .toList();
        return jpaStudentCourseLinkRepository.saveAll(studentCourseLinkModels)
                .stream()
                .map(StudentCourseLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public Course createStudentCourseLink(String studentId, String courseId, String tenantId) {

        CourseModel courseModel = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        StudentModel studentModel = jpaStudentRepository.getReferenceById(UUID.fromString(studentId));
        StudentCourseLinkModel studentCourseLinkModel = StudentCourseLinkModel
                .builder()
                .course(courseModel)
                .student(studentModel)
                .tenantId(tenantId)
                .build();
        jpaStudentCourseLinkRepository.save(studentCourseLinkModel);
        return CourseMapper.INSTANCE.toDomainEntity(courseModel); // TODO: should be populated from save()
    }

    @Override
    public Map<String, List<Student>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<Student>> result = new HashMap<>();
        for (Object[] row : jpaStudentLessonLinkRepository.findStudentsByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            StudentModel studentModel = (StudentModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(StudentMapper.INSTANCE.toDomainEntity(studentModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Student>> findAllByLessonGroupIds(List<String> lessonGroupIds) {
        if (lessonGroupIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonGroupIds.stream().map(UUID::fromString).toList();
        Map<String, List<Student>> result = new HashMap<>();
        for (Object[] row : jpaStudentLessonLinkRepository.findStudentsByLessonGroupIds(uuids)) {
            String lessonGroupId = row[0].toString();
            StudentModel studentModel = (StudentModel) row[1];
            result.computeIfAbsent(lessonGroupId, k -> new ArrayList<>())
                    .add(StudentMapper.INSTANCE.toDomainEntity(studentModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Student>> findAllByCourseIds(List<String> courseIds) {
        if (courseIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = courseIds.stream().map(UUID::fromString).toList();
        Map<String, List<Student>> result = new HashMap<>();
        for (Object[] row : jpaStudentCourseLinkRepository.findStudentsByCourseIds(uuids)) {
            String courseId = row[0].toString();
            StudentModel studentModel = (StudentModel) row[1];
            result.computeIfAbsent(courseId, k -> new ArrayList<>())
                    .add(StudentMapper.INSTANCE.toDomainEntity(studentModel));
        }
        return result;
    }

    @Override
    public Map<String, String> findUserIdsByStudentIds(List<String> studentIds) {
        if (studentIds.isEmpty()) return Map.of();

        List<UUID> uuids = studentIds.stream().map(UUID::fromString).toList();
        List<Object[]> results = jpaStudentRepository.findUserIdsByIds(uuids);

        Map<String, String> map = new HashMap<>();
        for (Object[] row : results) {
            map.put(row[0].toString(), (String) row[1]);
        }
        return map;
    }

    @Override
    public void deleteAllStudentCourseLinksByCourseId(String courseId) {
        jpaStudentCourseLinkRepository.deleteAllByCourseId(UUID.fromString(courseId));
    }

    @Override
    public List<Student> findAllByTenantId(String tenantId) {
        return jpaStudentRepository.findByTenantId(tenantId).stream()
                .map(StudentMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaStudentRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllStudentCourseLinksByStudentId(String studentId) {
        jpaStudentCourseLinkRepository.deleteAllByStudentId(UUID.fromString(studentId));
    }

    @Override
    public void deleteAllStudentOutletLinksByStudentId(String studentId) {
        jpaStudentOutletLinkRepository.deleteAllByStudentId(UUID.fromString(studentId));
    }

    @Override
    public void deleteAllStudentLessonLinksByStudentId(String studentId) {
        jpaStudentLessonLinkRepository.deleteAllByStudentId(UUID.fromString(studentId));
    }

    @Override
    public void deleteAllSubjectStudentLinksByStudentId(String studentId) {
        jpaSubjectStudentLinkRepository.deleteAllByStudentId(UUID.fromString(studentId));
    }

    @Override
    public void deleteAllAccountStudentLinksByStudentId(String studentId) {
        jpaAccountStudentLinkRepository.deleteAllByStudentId(UUID.fromString(studentId));
    }

    @Override
    public Map<String, Level> findLevelsByStudentIds(List<String> studentIds) {
        if (studentIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = studentIds.stream().map(UUID::fromString).toList();
        Map<String, Level> result = new HashMap<>();
        for (Object[] row : jpaStudentRepository.findLevelsByStudentIds(uuids)) {
            String studentId = row[0].toString();
            LevelModel levelModel = (LevelModel) row[1];
            result.put(studentId, LevelMapper.INSTANCE.toDomainEntity(levelModel));
        }
        return result;
    }

    @Override
    public Map<String, School> findSchoolsByStudentIds(List<String> studentIds) {
        if (studentIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = studentIds.stream().map(UUID::fromString).toList();
        Map<String, School> result = new HashMap<>();
        for (Object[] row : jpaStudentRepository.findSchoolsByStudentIds(uuids)) {
            String studentId = row[0].toString();
            SchoolModel schoolModel = (SchoolModel) row[1];
            result.put(studentId, SchoolMapper.INSTANCE.toDomainEntity(schoolModel));
        }
        return result;
    }

    private StudentModel toAttachedModel(Student student) {
        StudentModel model = StudentMapper.INSTANCE.toModelEntity(student);
        model.setSchool(Refs.ref(entityManager, SchoolModel.class, student.getSchoolId()));
        model.setLevel(Refs.ref(entityManager, LevelModel.class, student.getLevelId()));
        return model;
    }
}
