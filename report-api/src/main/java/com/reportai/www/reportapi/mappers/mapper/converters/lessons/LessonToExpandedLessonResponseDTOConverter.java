package com.reportai.www.reportapi.mappers.mapper.converters.lessons;

import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.outletrooms.response.OutletRoomResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.views.LessonView;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.converters.courses.CourseToCourseResponseConverter;
import com.reportai.www.reportapi.mappers.mapper.converters.students.StudentToStudentResponseDTOConverter;
import com.reportai.www.reportapi.repositories.views.LessonViewRepository;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonToExpandedLessonResponseDTOConverter implements Converter<Lesson, ExpandedLessonResponseDTO>, ModelMapperConfigurer {

    private final StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter;
    private final LessonViewRepository lessonViewRepository;
    private final CourseToCourseResponseConverter courseToCourseResponseConverter;

    @Autowired
    public LessonToExpandedLessonResponseDTOConverter(StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter,
                                                      LessonViewRepository lessonViewRepository, CourseToCourseResponseConverter courseToCourseResponseConverter) {
        this.studentToStudentResponseDTOConverter = studentToStudentResponseDTOConverter;
        this.lessonViewRepository = lessonViewRepository;
        this.courseToCourseResponseConverter = courseToCourseResponseConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedLessonResponseDTO convert(MappingContext<Lesson, ExpandedLessonResponseDTO> mappingContext) {
        Lesson lesson = mappingContext.getSource();
        LessonView lessonView = lessonViewRepository.findById(lesson.getId()).orElseThrow(() -> new ResourceNotFoundException("lesson view not found during mapping"));
        List<StudentResponseDTO> studentResponseDTOs = lesson
                .getStudentLessonRegistrations()
                .stream()
                .map(StudentLessonRegistration::getStudent)
                .map(student -> studentToStudentResponseDTOConverter.convert(mappingContext.create(student, StudentResponseDTO.class)))
                .toList();
        List<EducatorResponseDTO> educatorResponseDTOs = lesson
                .getEducatorLessonAttachments()
                .stream()
                .map(EducatorLessonAttachment::getEducator)
                .map(educator -> mappingContext
                        .getMappingEngine()
                        .map(mappingContext.create(educator, EducatorResponseDTO.class)))
                .toList();
        CourseResponseDTO courseResponseDTO = lesson.getCourse() != null ? courseToCourseResponseConverter.convert(mappingContext.create(lesson.getCourse(), CourseResponseDTO.class)) : null;
        SubjectResponseDTO subjectResponseDTO = lesson.getSubject() != null ? mappingContext.getMappingEngine().map(mappingContext.create(lesson.getSubject(), SubjectResponseDTO.class)) : null;
        OutletRoomResponseDTO outletRoomResponseDTO = lesson.getLessonOutletRoomBooking() != null ? mappingContext
                .getMappingEngine()
                .map(mappingContext.create(lesson.getLessonOutletRoomBooking().getOutletRoom(), OutletRoomResponseDTO.class)) : null;
        return ExpandedLessonResponseDTO
                .builder()
                .id(lessonView.getId().toString())
                .name(lessonView.getName())
                .description(lessonView.getDescription())
                .lessonStatus(lessonView.getLessonStatus())
                .lessonReviewStatus(lessonView.getLessonReviewStatus())
                .lessonPlanStatus(lessonView.getLessonPlanStatus())
                .lessonStartTimestamptz(lessonView.getLessonStartTimestamptz())
                .lessonEndTimestamptz(lessonView.getLessonEndTimestamptz())
                .students(studentResponseDTOs)
                .educators(educatorResponseDTOs)
                .course(courseResponseDTO)
                .subject(subjectResponseDTO)
                .outletRoom(outletRoomResponseDTO)
                .build();
    }
}
