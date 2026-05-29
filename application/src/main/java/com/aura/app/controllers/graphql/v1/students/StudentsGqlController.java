package com.aura.app.controllers.graphql.v1.students;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.DeleteStudentByIdInput;
import com.aura.app.codegen.types.GetStudentByIdInput;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.app.codegen.types.SchoolResponse;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.app.codegen.types.UpdateStudentByIdInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.CourseMapper;
import com.aura.app.mappers.LevelMapper;
import com.aura.app.mappers.SchoolMapper;
import com.aura.app.mappers.StudentMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.details.services.StudentService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StudentsGqlController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<StudentResponse> getAllStudentsInInstitution(@Argument("institutionId") String institutionId,
                                                             @ContextValue("user") User user) {
        return studentService.findAllByTenantId(institutionId).stream()
                .map(StudentMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'view')")
    @QueryMapping
    public StudentResponse getStudentById(@Argument("input") GetStudentByIdInput input,
                                          @ContextValue("user") User user) {
        return Optional.ofNullable(studentService.findById(input.getId()))
                .map(StudentMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Student"));
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public StudentResponse updateStudentById(@Argument("input") UpdateStudentByIdInput input,
                                             @ContextValue("user") User user) {
        Student updated = studentService.updateById(
                input.getId(),
                input.getName(),
                input.getEmail(),
                input.getDateOfBirth());
        return StudentMapper.INSTANCE.toDto(updated);
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'delete')")
    @MutationMapping
    public Boolean deleteStudentById(@Argument("input") DeleteStudentByIdInput input,
                                     @ContextValue("user") User user) {
        studentService.deleteById(input.getId());
        return true;
    }

    @BatchMapping(typeName = "StudentResponse", field = "level")
    public Map<StudentResponse, LevelResponse> level(List<StudentResponse> students) {
        List<String> studentIds = students.stream().map(StudentResponse::getId).toList();
        Map<String, Level> levelByStudentId = studentRepository.findLevelsByStudentIds(studentIds);
        return students.stream()
                .filter(s -> levelByStudentId.containsKey(s.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> LevelMapper.INSTANCE.mapToDto(levelByStudentId.get(s.getId()))));
    }

    @BatchMapping(typeName = "StudentResponse", field = "school")
    public Map<StudentResponse, SchoolResponse> school(List<StudentResponse> students) {
        List<String> studentIds = students.stream().map(StudentResponse::getId).toList();
        Map<String, School> schoolByStudentId = studentRepository.findSchoolsByStudentIds(studentIds);
        return students.stream()
                .filter(s -> schoolByStudentId.containsKey(s.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> SchoolMapper.INSTANCE.toDto(schoolByStudentId.get(s.getId()))));
    }

    @BatchMapping(typeName = "StudentResponse", field = "courses")
    public Map<StudentResponse, List<CourseResponse>> courses(List<StudentResponse> students) {
        List<String> studentIds = students.stream().map(StudentResponse::getId).toList();
        Map<String, List<Course>> coursesByStudentId = courseRepository.findAllByStudentIds(studentIds);
        return students.stream().collect(Collectors.toMap(
                Function.identity(),
                s -> coursesByStudentId.getOrDefault(s.getId(), List.of()).stream()
                        .map(CourseMapper.INSTANCE::toDto).toList()));
    }
}
