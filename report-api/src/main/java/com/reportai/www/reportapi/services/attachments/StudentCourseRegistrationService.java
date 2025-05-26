package com.reportai.www.reportapi.services.attachments;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.attachments.StudentCourseRegistration;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.repositories.StudentCourseRegistrationRepository;
import com.reportai.www.reportapi.services.common.ISimpleAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.students.StudentsService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class StudentCourseRegistrationService implements ISimpleAttachmentService<Student, Course> {
    private final StudentsService studentsService;

    private final CoursesService coursesService;

    private final StudentCourseRegistrationRepository studentCourseRegistrationRepository;

    @Autowired
    public StudentCourseRegistrationService(StudentsService studentsService, CoursesService coursesService, StudentCourseRegistrationRepository studentCourseRegistrationRepository) {
        this.studentsService = studentsService;
        this.coursesService = coursesService;
        this.studentCourseRegistrationRepository = studentCourseRegistrationRepository;
    }

    @Override
    public ISimpleRead<Student> getEntity1ReadService() {
        return studentsService;
    }

    @Override
    public ISimpleRead<Course> getEntity2ReadService() {
        return coursesService;
    }

    @Override
    public JpaRepository<StudentCourseRegistration, UUID> getAttachmentRepository() {
        return studentCourseRegistrationRepository;
    }
}
