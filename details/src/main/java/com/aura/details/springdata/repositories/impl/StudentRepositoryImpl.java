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
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import com.aura.details.springdata.tables.courses.CourseTable;
import com.aura.details.springdata.tables.levels.LevelTable;
import com.aura.details.springdata.tables.links.StudentCourseLinkTable;
import com.aura.details.springdata.tables.schools.SchoolTable;
import com.aura.details.springdata.tables.students.StudentTable;
import java.util.List;
import java.util.UUID;

public class StudentRepositoryImpl implements StudentRepository {

    private final JpaStudentRepository jpaStudentRepository;
    private final JpaLevelRepository jpaLevelRepository;
    private final JpaSchoolRepository jpaSchoolRepository;
    private final JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository;
    private final JpaCourseRepository jpaCourseRepository;


    public StudentRepositoryImpl(JpaStudentRepository jpaStudentRepository, JpaLevelRepository jpaLevelRepository, JpaSchoolRepository jpaSchoolRepository, JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository, JpaCourseRepository jpaCourseRepository) {
        this.jpaStudentRepository = jpaStudentRepository;
        this.jpaLevelRepository = jpaLevelRepository;
        this.jpaSchoolRepository = jpaSchoolRepository;
        this.jpaStudentCourseLinkRepository = jpaStudentCourseLinkRepository;
        this.jpaCourseRepository = jpaCourseRepository;
    }

    @Override
    public Student save(Student student) {
        StudentTable studentTable = StudentMapper.INSTANCE.toTableEntity(student);
        StudentTable createdStudentTable = jpaStudentRepository.save(studentTable);
        return StudentMapper.INSTANCE.toDomainEntity(createdStudentTable);
    }

    @Override
    public Student findById(String id) {
        StudentTable studentTable = jpaStudentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new RuntimeException("not found"));
        return StudentMapper.INSTANCE.toDomainEntity(studentTable);
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        List<StudentTable> studentTables = students.stream().map(StudentMapper.INSTANCE::toTableEntity).toList();
        List<StudentTable> createdStudentTables = jpaStudentRepository.saveAll(studentTables);
        return createdStudentTables.stream().map(StudentMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public Level tagStudentToLevelByName(String studentId, String levelName, String tenantId) {
        LevelTable level = jpaLevelRepository.findByNameAndTenantId(levelName, tenantId);
        StudentTable studentTable = jpaStudentRepository.findById(UUID.fromString(studentId)).orElseThrow(() -> new RuntimeException("not found"));
        studentTable.setLevel(level);
        jpaStudentRepository.save(studentTable);
        return LevelMapper.INSTANCE.toDomainEntity(level);
    }

    @Override
    public School tagStudentToSchoolByName(String studentId, String schoolName, String tenantId) {
        SchoolTable school = jpaSchoolRepository.findByNameAndTenantId(schoolName, tenantId);
        StudentTable studentTable = jpaStudentRepository.findById(UUID.fromString(studentId)).orElseThrow(() -> new RuntimeException("not found"));
        studentTable.setSchool(school);
        jpaStudentRepository.save(studentTable);
        return SchoolMapper.INSTANCE.toDomainEntity(school);
    }

    @Override
    public Course createStudentCourseLink(String studentId, String courseId) {

        CourseTable courseTable = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        StudentTable studentTable = jpaStudentRepository.getReferenceById(UUID.fromString(studentId));
        StudentCourseLinkTable studentCourseLinkTable = StudentCourseLinkTable
                .builder()
                .course(courseTable)
                .student(studentTable)
                .build();
        jpaStudentCourseLinkRepository.save(studentCourseLinkTable);
        return CourseMapper.INSTANCE.toDomainEntity(courseTable); // TODO: should be populated from save()
    }
}
