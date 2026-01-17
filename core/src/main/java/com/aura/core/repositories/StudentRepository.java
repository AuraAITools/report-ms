package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface StudentRepository {
    Student save(Student student);

    @Nullable Student findById(String id);

    List<Student> saveAll(List<Student> students);

    Level tagStudentToLevel(String studentId, String levelId, String tenantId);

    School tagStudentToSchool(String studentId, String schoolId, String tenantId);

    Course createStudentCourseLink(String studentId, String courseId, String tenantId);

}
