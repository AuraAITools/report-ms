package com.reportai.www.reportapi.services.attachments;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.SubjectCourseAttachment;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.repositories.SubjectCourseAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class SubjectCourseAttachmentService implements ISimpleAttachmentService<Subject, Course> {
    private final SubjectsService subjectsService;
    private final CoursesService coursesService;
    private final SubjectCourseAttachmentRepository subjectCourseAttachmentRepository;

    @Autowired
    public SubjectCourseAttachmentService(SubjectsService subjectsService, CoursesService coursesService, SubjectCourseAttachmentRepository subjectCourseAttachmentRepository) {
        this.subjectsService = subjectsService;
        this.coursesService = coursesService;
        this.subjectCourseAttachmentRepository = subjectCourseAttachmentRepository;
    }

    @Override
    public ISimpleRead<Subject> getEntity1ReadService() {
        return subjectsService;
    }

    @Override
    public ISimpleRead<Course> getEntity2ReadService() {
        return coursesService;
    }

    @Override
    public JpaRepository<SubjectCourseAttachment, UUID> getAttachmentRepository() {
        return subjectCourseAttachmentRepository;
    }
}
