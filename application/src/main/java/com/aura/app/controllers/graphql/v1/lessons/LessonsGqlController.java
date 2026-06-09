package com.aura.app.controllers.graphql.v1.lessons;

import com.aura.app.codegen.types.ChangeLessonOutletRoomInput;
import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.CreateLessonInCourseInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.app.codegen.types.LessonGroupResponse;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.app.codegen.types.MaterialResponse;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.codegen.types.OutletRoomResponse;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.CourseMapper;
import com.aura.app.mappers.EducatorMapper;
import com.aura.app.mappers.LessonGroupMapper;
import com.aura.app.mappers.LessonGroupPlanMapper;
import com.aura.app.mappers.LessonMapper;
import com.aura.app.mappers.LevelMapper;
import com.aura.app.mappers.MaterialMapper;
import com.aura.app.mappers.OutletMapper;
import com.aura.app.mappers.OutletRoomMapper;
import com.aura.app.mappers.StudentMapper;
import com.aura.app.mappers.SubjectMapper;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.details.services.LessonGroupPlanService;
import com.aura.details.services.LessonService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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


import static com.aura.app.codegen.DgsConstants.MUTATION.CreateLessonInCourse;

@Controller
@RequiredArgsConstructor
public class LessonsGqlController {
    private final LessonService lessonService;
    private final LessonRepository lessonRepository;
    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    private final OutletRepository outletRepository;
    private final OutletRoomRepository outletRoomRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final LevelRepository levelRepository;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final LessonGroupRepository lessonGroupRepository;
    private final MaterialRepository materialRepository;

    @PreAuthorize("hasPermission(#createLessonInCourseInput.courseId, 'course', 'create_lesson')")
    @MutationMapping(CreateLessonInCourse)
    public LessonResponse createLessonInCourse(@Argument(name = CreateLessonInCourse) CreateLessonInCourseInput createLessonInCourseInput, @ContextValue(name = "user") User user) {
        Lesson lessonToBeCreated = LessonMapper.INSTANCE.toDomainEntity(createLessonInCourseInput);
        List<LessonService.NewLessonGroup> newLessonGroups = createLessonInCourseInput.getLessonGroups().stream()
                .map(g -> new LessonService.NewLessonGroup(
                        g.getName(),
                        LessonGroupPlanMapper.INSTANCE.toDomainEntity(g.getPlan()),
                        g.getPlan().getTopicIds(),
                        g.getStudentIds(),
                        g.getPlan().getMaterialIds(),
                        g.getPlan().getLessonObjectives().stream()
                                .map(o -> new LessonGroupPlanService.NewLessonObjective(
                                        LessonGroupPlanMapper.INSTANCE.toDomainEntity(o), o.getTopicIds()))
                                .toList()))
                .toList();
        Lesson lesson = lessonService.createLesson(
                lessonToBeCreated,
                createLessonInCourseInput.getCourseId(),
                createLessonInCourseInput.getOutletRoomId(),
                createLessonInCourseInput.getEducatorIds(),
                createLessonInCourseInput.getStudentIds(),
                createLessonInCourseInput.getSubjectIds(),
                createLessonInCourseInput.getLevelIds(),
                createLessonInCourseInput.getTopicIds(),
                newLessonGroups);
        return LessonMapper.INSTANCE.toResponseDto(lesson);
    }

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public LessonResponse getLessonById(@Argument("lessonId") String lessonId,
                                        @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonService.findById(UUID.fromString(lessonId)))
                .map(LessonMapper.INSTANCE::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Lesson"));
    }

    @PreAuthorize("hasPermission(#courseId, 'course', 'view')")
    @QueryMapping
    public List<LessonResponse> getAllLessonsInCourse(@Argument("courseId") String courseId,
                                                      @ContextValue(name = "user") User user) {
        return lessonService.findAllByCourseId(courseId).stream()
                .map(LessonMapper.INSTANCE::toResponseDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")
    @QueryMapping
    public List<LessonResponse> getAllLessonsInOutlet(@Argument("outletId") String outletId,
                                                      @ContextValue(name = "user") User user) {
        return lessonService.findAllByOutletId(outletId).stream()
                .map(LessonMapper.INSTANCE::toResponseDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<LessonResponse> getLessonsByEducatorId(@Argument("institutionId") String institutionId,
                                                       @Argument("educatorId") String educatorId,
                                                       @ContextValue(name = "user") User user) {
        return lessonService.findAllByEducatorId(educatorId).stream()
                .map(LessonMapper.INSTANCE::toResponseDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<LessonResponse> getLessonsByStudentId(@Argument("institutionId") String institutionId,
                                                      @Argument("studentId") String studentId,
                                                      @ContextValue(name = "user") User user) {
        return lessonService.findAllByStudentId(studentId).stream()
                .map(LessonMapper.INSTANCE::toResponseDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#id, 'lesson', 'delete')")
    @MutationMapping
    public Boolean deleteLessonById(@Argument("id") String id, @ContextValue(name = "user") User user) {
        if (lessonService.findById(UUID.fromString(id)) == null) {
            throw new ResourceNotFoundBusinessException("Lesson");
        }
        lessonService.deleteById(id);
        return true;
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonResponse changeLessonOutletRoom(@Argument("input") ChangeLessonOutletRoomInput input,
                                                 @ContextValue(name = "user") User user) {
        Lesson lesson = lessonService.changeLessonOutletRoom(input.getLessonId(), input.getOutletRoomId());
        return LessonMapper.INSTANCE.toResponseDto(lesson);
    }

    @BatchMapping(typeName = "LessonResponse", field = "course")
    public Map<LessonResponse, CourseResponse> course(List<LessonResponse> lessonResponses) {
        List<UUID> lessonIds = lessonResponses.stream().map(l -> UUID.fromString(l.getId())).toList();

        Map<String, Lesson> lessonIdToLesson = lessonRepository.findAllById(lessonIds).stream()
                .collect(Collectors.toMap(Lesson::getId, Function.identity()));

        Set<String> courseIds = lessonIdToLesson.values().stream()
                .map(Lesson::getCourseId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, CourseResponse> courseIdToCourse = courseRepository.findAllByIds(List.copyOf(courseIds)).stream()
                .collect(Collectors.toMap(Course::getId, CourseMapper.INSTANCE::toDto));

        return lessonResponses.stream()
                .filter(l -> {
                    Lesson lesson = lessonIdToLesson.get(l.getId());
                    return lesson != null && lesson.getCourseId() != null
                            && courseIdToCourse.containsKey(lesson.getCourseId());
                })
                .collect(Collectors.toMap(
                        Function.identity(),
                        l -> courseIdToCourse.get(lessonIdToLesson.get(l.getId()).getCourseId())
                ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "outlet")
    public Map<LessonResponse, OutletResponse> outlet(List<LessonResponse> lessonResponses) {
        List<UUID> lessonIds = lessonResponses.stream().map(l -> UUID.fromString(l.getId())).toList();

        Map<String, Lesson> lessonIdToLessonMap = lessonRepository.findAllById(lessonIds)
                .stream()
                .collect(Collectors.toMap(Lesson::getId, Function.identity()));

        Set<String> outletIds = lessonIdToLessonMap.values().stream()
                .map(Lesson::getOutletId)
                .collect(Collectors.toSet());

        Map<String, OutletResponse> outletIdToOutletMap = outletRepository.findAllByIds(List.copyOf(outletIds)).stream()
                .collect(Collectors.toMap(Outlet::getId, OutletMapper.INSTANCE::toDto));

        return lessonIdToLessonMap
                .values()
                .stream()
                .collect(Collectors.toMap(LessonMapper.INSTANCE::toResponseDto, lesson -> outletIdToOutletMap.get(lesson.getOutletId())));
    }

    @BatchMapping(typeName = "LessonResponse", field = "outletRoom")
    public Map<LessonResponse, OutletRoomResponse> outletRoom(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, LessonOutletRoomBooking> lessonIdToBooking =
                lessonOutletRoomBookingRepository.findAllByLessonIds(lessonIds);

        List<UUID> outletRoomIds = lessonIdToBooking.values().stream()
                .map(LessonOutletRoomBooking::getOutletRoomId)
                .filter(java.util.Objects::nonNull)
                .map(UUID::fromString)
                .toList();

        Map<String, OutletRoom> outletRoomIdToRoom = outletRoomRepository.findAllByIds(outletRoomIds).stream()
                .collect(Collectors.toMap(OutletRoom::getId, Function.identity()));

        Map<LessonResponse, OutletRoomResponse> result = new java.util.HashMap<>();
        for (LessonResponse lessonResponse : lessonResponses) {
            LessonOutletRoomBooking booking = lessonIdToBooking.get(lessonResponse.getId());
            if (booking == null) continue;
            OutletRoom room = outletRoomIdToRoom.get(booking.getOutletRoomId());
            if (room == null) continue;
            result.put(lessonResponse, OutletRoomMapper.INSTANCE.toDto(room));
        }
        return result;
    }

    @BatchMapping(typeName = "LessonResponse", field = "educators")
    public Map<LessonResponse, List<EducatorResponse>> educators(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<Educator>> lessonIdToEducatorListsMap = educatorRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToEducatorListsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(EducatorMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "students")
    public Map<LessonResponse, List<StudentResponse>> students(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<Student>> lessonIdToStudentListsMap = studentRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToStudentListsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(StudentMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "subjects")
    public Map<LessonResponse, List<SubjectResponse>> subjects(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<Subject>> lessonIdToSubjectListsMap = subjectRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToSubjectListsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(SubjectMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "levels")
    public Map<LessonResponse, List<LevelResponse>> levels(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<Level>> lessonIdToLevelsMap = levelRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToLevelsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(LevelMapper.INSTANCE::mapToDto).toList()
        ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "topics")
    public Map<LessonResponse, List<TopicResponse>> topics(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<Topic>> lessonIdToTopicsMap = topicRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToTopicsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(TopicMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "lessonGroups")
    public Map<LessonResponse, List<LessonGroupResponse>> lessonGroups(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<LessonGroup>> lessonIdToGroupsMap = lessonGroupRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToGroupsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(LessonGroupMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "LessonResponse", field = "materials")
    public Map<LessonResponse, List<MaterialResponse>> materials(List<LessonResponse> lessonResponses) {
        List<String> lessonIds = lessonResponses.stream().map(LessonResponse::getId).toList();
        Map<String, List<Material>> lessonIdToMaterialsMap = materialRepository.findAllByLessonIds(lessonIds);
        return lessonResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                l -> lessonIdToMaterialsMap.getOrDefault(l.getId(), List.of()).stream()
                        .map(MaterialMapper.INSTANCE::toDto).toList()
        ));
    }
}
