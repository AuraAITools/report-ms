package com.aura.app.controllers.graphql.v1.courses;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.CreateCourseInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.CourseMapper;
import com.aura.app.mappers.EducatorMapper;
import com.aura.app.mappers.LessonMapper;
import com.aura.app.mappers.LevelMapper;
import com.aura.app.mappers.StudentMapper;
import com.aura.app.mappers.SubjectMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.services.CourseService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATECOURSE_INPUT_ARGUMENT.CreateCourseInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateCourse;

@Controller
@RequiredArgsConstructor
public class CourseGqlController {
    private final CourseService courseService;
    private final LessonRepository lessonRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final LevelRepository levelRepository;

    @PreAuthorize("hasPermission(#createCourseInput.outletId, 'outlet', 'create_course')")
    @MutationMapping(CreateCourse)
    public CourseResponse createCourse(@Argument(name = CreateCourseInput) CreateCourseInput createCourseInput, @ContextValue(name = "user") User user) {
        Course newCourse = CourseMapper.INSTANCE.toDomainEntity(createCourseInput);
        Course createdCourse = courseService.createCourseInOutlet(
                newCourse,
                createCourseInput.getOutletId(),
                createCourseInput.getSubjectIds(),
                createCourseInput.getEducatorIds(),
                createCourseInput.getStudentIds());
        return CourseMapper.INSTANCE.toDto(createdCourse);
    }

    @PreAuthorize("hasPermission(#courseId, 'course', 'view')")
    @QueryMapping
    public CourseResponse getCourseById(@Argument("courseId") String courseId,
                                        @ContextValue(name = "user") User user) {
        return Optional.ofNullable(courseService.findById(courseId))
                .map(CourseMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Course"));
    }

    @PreAuthorize("hasPermission(#id, 'course', 'delete')")
    @MutationMapping
    public Boolean deleteCourseById(@Argument("id") String id, @ContextValue(name = "user") User user) {
        if (courseService.findById(id) == null) {
            throw new ResourceNotFoundBusinessException("Course");
        }
        courseService.deleteById(id);
        return true;
    }

    @PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")
    @QueryMapping
    public List<CourseResponse> getAllCoursesInOutlet(@Argument("outletId") String outletId,
                                                      @ContextValue(name = "user") User user) {
        return courseService.findAllByOutletId(outletId).stream()
                .map(CourseMapper.INSTANCE::toDto)
                .toList();
    }

    @BatchMapping(typeName = "CourseResponse", field = "lessons")
    public Map<CourseResponse, List<LessonResponse>> lessons(List<CourseResponse> courseResponses) {
        List<String> courseIds = courseResponses.stream().map(CourseResponse::getId).toList();
        Map<String, List<Lesson>> lessonsByCourseId = lessonRepository.findAllByCourseIds(courseIds);
        return courseResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                c -> lessonsByCourseId.getOrDefault(c.getId(), List.of()).stream()
                        .map(LessonMapper.INSTANCE::toResponseDto).toList()
        ));
    }

    @BatchMapping(typeName = "CourseResponse", field = "educators")
    public Map<CourseResponse, List<EducatorResponse>> educators(List<CourseResponse> courseResponses) {
        List<String> courseIds = courseResponses.stream().map(CourseResponse::getId).toList();
        Map<String, List<Educator>> educatorsByCourseId = educatorRepository.findAllByCourseIds(courseIds);
        return courseResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                c -> educatorsByCourseId.getOrDefault(c.getId(), List.of()).stream()
                        .map(EducatorMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "CourseResponse", field = "students")
    public Map<CourseResponse, List<StudentResponse>> students(List<CourseResponse> courseResponses) {
        List<String> courseIds = courseResponses.stream().map(CourseResponse::getId).toList();
        Map<String, List<Student>> studentsByCourseId = studentRepository.findAllByCourseIds(courseIds);
        return courseResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                c -> studentsByCourseId.getOrDefault(c.getId(), List.of()).stream()
                        .map(StudentMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "CourseResponse", field = "subjects")
    public Map<CourseResponse, List<SubjectResponse>> subjects(List<CourseResponse> courseResponses) {
        List<String> courseIds = courseResponses.stream().map(CourseResponse::getId).toList();
        Map<String, List<Subject>> subjectsByCourseId = subjectRepository.findAllByCourseIds(courseIds);
        return courseResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                c -> subjectsByCourseId.getOrDefault(c.getId(), List.of()).stream()
                        .map(SubjectMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "CourseResponse", field = "level")
    public Map<CourseResponse, LevelResponse> level(List<CourseResponse> courseResponses) {
        List<String> courseIds = courseResponses.stream().map(CourseResponse::getId).toList();
        Map<String, Level> levelByCourseId = levelRepository.findAllByCourseIds(courseIds);
        return courseResponses.stream()
                .filter(c -> levelByCourseId.containsKey(c.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        c -> LevelMapper.INSTANCE.mapToDto(levelByCourseId.get(c.getId()))
                ));
    }
}
