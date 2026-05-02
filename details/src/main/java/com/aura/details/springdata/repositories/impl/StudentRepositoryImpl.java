package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import com.aura.details.springdata.mappers.students.StudentMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
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

    @Override
    public Student save(Student student) {
        StudentModel studentModel = StudentMapper.INSTANCE.toModelEntity(student);
        StudentModel createdStudentModel = jpaStudentRepository.save(studentModel);
        return StudentMapper.INSTANCE.toDomainEntity(createdStudentModel);
    }

    @Override
    public @Nullable Student findById(String id) {
        StudentModel studentModel = jpaStudentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new RuntimeException("not found"));
        return StudentMapper.INSTANCE.toDomainEntity(studentModel);
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        List<StudentModel> studentModels = students.stream().map(StudentMapper.INSTANCE::toModelEntity).toList();
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
}
