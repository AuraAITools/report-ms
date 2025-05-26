package com.reportai.www.reportapi.services.attachments;

import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.repositories.EducatorCourseAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class EducatorCourseAttachmentService implements ISimpleAttachmentService<Educator, Course> {
    private final EducatorsService educatorsService;
    private final CoursesService coursesService;
    private final EducatorCourseAttachmentRepository educatorCourseAttachmentRepository;

    @Autowired
    public EducatorCourseAttachmentService(EducatorsService educatorsService, CoursesService coursesService, EducatorCourseAttachmentRepository educatorCourseAttachmentRepository) {
        this.educatorsService = educatorsService;
        this.coursesService = coursesService;
        this.educatorCourseAttachmentRepository = educatorCourseAttachmentRepository;
    }

    @Override
    public ISimpleRead<Educator> getEntity1ReadService() {
        return educatorsService;
    }

    @Override
    public ISimpleRead<Course> getEntity2ReadService() {
        return coursesService;
    }

    @Override
    public JpaRepository<EducatorCourseAttachment, UUID> getAttachmentRepository() {
        return educatorCourseAttachmentRepository;
    }
}
