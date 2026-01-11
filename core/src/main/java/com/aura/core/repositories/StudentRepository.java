package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import java.util.List;

public interface StudentRepository {
    Student save(Student student);

    Student findById(String id);

    List<Student> saveAll(List<Student> students);

    Level tagStudentToLevelByName(String studentId, String levelName, String tenantId);

    School tagStudentToSchoolByName(String studentId, String schoolName, String tenantId);

    Course createStudentCourseLink(String studentId, String courseId);

}
