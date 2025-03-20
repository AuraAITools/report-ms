package com.reportai.www.reportapi.services.lessons;

import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Lesson;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.repositories.LessonRepository;
import com.reportai.www.reportapi.repositories.specifications.TenantSpecification;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonsService implements BaseServiceTemplate<Lesson, UUID> {

    private final LessonRepository lessonRepository;

    private final CoursesService coursesService;

    private final ModelMapper modelMapper;

    private final EducatorsService educatorsService;

    private final StudentsService studentsService;

    private final SubjectsService subjectsService;

    @Autowired
    public LessonsService(LessonRepository lessonRepository, @Lazy CoursesService coursesService, EducatorsService educatorsService, StudentsService studentsService, SubjectsService subjectsService) {
        this.lessonRepository = lessonRepository;
        this.coursesService = coursesService;
        this.educatorsService = educatorsService;
        this.studentsService = studentsService;
        this.subjectsService = subjectsService;
        this.modelMapper = new ModelMapper();
        this.modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);// skip null values
        // TODO: refactor Converters and Configurers to be in separate files instead of in the constructor
        // Educators converter with null check
        Converter<List<UUID>, List<Educator>> educatorIdsToEducatorsConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            List<UUID> educatorIds = context.getSource();
            return educatorIds.stream()
                    .map(educatorsService::findById)
                    .collect(Collectors.toList());
        };

        // Students converter with null check
        Converter<List<UUID>, List<Student>> studentIdsToStudentsConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            List<UUID> studentIds = context.getSource();
            return studentIds.stream()
                    .map(studentsService::findById)
                    .collect(Collectors.toList());
        };

        // Subject converter with null check that properly looks up the subject
        Converter<UUID, Subject> subjectIdToSubjectConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            UUID subjectId = context.getSource();
            return subjectsService.findById(subjectId);
        };

        this.modelMapper
                .typeMap(UpdateLessonRequestDTO.class, Lesson.class)
                .addMappings(mapper -> {
                    mapper.using(educatorIdsToEducatorsConverter)
                            .map(UpdateLessonRequestDTO::getEducatorIds, Lesson::setEducators);
                    mapper.using(studentIdsToStudentsConverter)
                            .map(UpdateLessonRequestDTO::getStudentIds, Lesson::setStudents);
                    mapper.using(subjectIdToSubjectConverter)
                            .map(UpdateLessonRequestDTO::getSubjectId, Lesson::setSubject);
                });
    }

    @Override
    public JpaRepository<Lesson, UUID> getRepository() {
        return lessonRepository;
    }

    /**
     * TODO: should be just create lesson not to course
     *
     * @param courseId
     * @param lesson
     * @param students
     * @param educators
     * @return
     */
    @Transactional
    public Lesson createLessonForCourse(@NonNull UUID courseId, Lesson lesson, List<Student> students, List<Educator> educators) {
        Course course = coursesService.findById(courseId);
        lesson.setOutlet(course.getOutlet());
        lesson.setCourse(course);
        lesson.setStudents(students);

        // Make sure educators are attached to the persistence context
        lesson.addEducators(educators);
        return lessonRepository.save(lesson);
    }

    /**
     * TODO: refactor
     *
     * @param institutionId
     * @return
     */
    @Transactional
    public List<Lesson> getExpandedLessonsInInstitution(@NonNull UUID institutionId) {
        return lessonRepository.findAll(TenantSpecification.forTenant(institutionId.toString()));
    }

    /**
     * updates lesson.
     * TODO: by right not good practice to have DTOs in the service layer or mapping logic,
     * but this is the easiest way rn
     *
     * @param updates
     * @return
     */
    @Transactional
    public Lesson update(UUID lessonId, UpdateLessonRequestDTO updates) {
        Lesson lesson = findById(lessonId);
        // model map to overlay the updates onto a existing lesson
        modelMapper.map(updates, lesson);
        return lessonRepository.save(lesson);
    }

}
