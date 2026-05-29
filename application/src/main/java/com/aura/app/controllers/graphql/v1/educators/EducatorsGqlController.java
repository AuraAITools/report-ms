package com.aura.app.controllers.graphql.v1.educators;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.DeleteEducatorByIdInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.app.codegen.types.GetEducatorByIdInput;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.UpdateEducatorByIdInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.AccountMapper;
import com.aura.app.mappers.CourseMapper;
import com.aura.app.mappers.EducatorMapper;
import com.aura.app.mappers.LevelMapper;
import com.aura.app.mappers.OutletMapper;
import com.aura.app.mappers.SubjectMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.educators.Educator.EMPLOYMENT_TYPE;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.services.EducatorService;
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
public class EducatorsGqlController {

    private final EducatorService educatorService;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final OutletRepository outletRepository;
    private final LevelRepository levelRepository;
    private final AccountRepository accountRepository;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<EducatorResponse> getAllEducatorsInInstitution(@Argument("institutionId") String institutionId,
                                                               @ContextValue("user") User user) {
        return educatorService.findAllByTenantId(institutionId).stream()
                .map(EducatorMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'view')")
    @QueryMapping
    public EducatorResponse getEducatorById(@Argument("input") GetEducatorByIdInput input,
                                            @ContextValue("user") User user) {
        return Optional.ofNullable(educatorService.findById(input.getId()))
                .map(EducatorMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Educator"));
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public EducatorResponse updateEducatorById(@Argument("input") UpdateEducatorByIdInput input,
                                               @ContextValue("user") User user) {
        EMPLOYMENT_TYPE employmentType = input.getEmploymentType() == null
                ? null
                : EMPLOYMENT_TYPE.valueOf(input.getEmploymentType().name());
        Educator updated = educatorService.updateById(
                input.getId(),
                input.getName(),
                input.getStartDate(),
                input.getDateOfBirth(),
                input.getEmail(),
                employmentType);
        return EducatorMapper.INSTANCE.toDto(updated);
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'delete')")
    @MutationMapping
    public Boolean deleteEducatorById(@Argument("input") DeleteEducatorByIdInput input,
                                      @ContextValue("user") User user) {
        educatorService.deleteById(input.getId());
        return true;
    }

    @BatchMapping(typeName = "EducatorResponse", field = "courses")
    public Map<EducatorResponse, List<CourseResponse>> courses(List<EducatorResponse> educators) {
        List<String> educatorIds = educators.stream().map(EducatorResponse::getId).toList();
        Map<String, List<Course>> coursesByEducatorId = courseRepository.findAllByEducatorIds(educatorIds);
        return educators.stream().collect(Collectors.toMap(
                Function.identity(),
                e -> coursesByEducatorId.getOrDefault(e.getId(), List.of()).stream()
                        .map(CourseMapper.INSTANCE::toDto).toList()));
    }

    @BatchMapping(typeName = "EducatorResponse", field = "subjects")
    public Map<EducatorResponse, List<SubjectResponse>> subjects(List<EducatorResponse> educators) {
        List<String> educatorIds = educators.stream().map(EducatorResponse::getId).toList();
        Map<String, List<Subject>> subjectsByEducatorId = subjectRepository.findAllByEducatorIds(educatorIds);
        return educators.stream().collect(Collectors.toMap(
                Function.identity(),
                e -> subjectsByEducatorId.getOrDefault(e.getId(), List.of()).stream()
                        .map(SubjectMapper.INSTANCE::toDto).toList()));
    }

    @BatchMapping(typeName = "EducatorResponse", field = "outlets")
    public Map<EducatorResponse, List<OutletResponse>> outlets(List<EducatorResponse> educators) {
        List<String> educatorIds = educators.stream().map(EducatorResponse::getId).toList();
        Map<String, List<Outlet>> outletsByEducatorId = outletRepository.findAllByEducatorIds(educatorIds);
        return educators.stream().collect(Collectors.toMap(
                Function.identity(),
                e -> outletsByEducatorId.getOrDefault(e.getId(), List.of()).stream()
                        .map(OutletMapper.INSTANCE::toDto).toList()));
    }

    @BatchMapping(typeName = "EducatorResponse", field = "levels")
    public Map<EducatorResponse, List<LevelResponse>> levels(List<EducatorResponse> educators) {
        List<String> educatorIds = educators.stream().map(EducatorResponse::getId).toList();
        Map<String, List<Level>> levelsByEducatorId = levelRepository.findAllByEducatorIds(educatorIds);
        return educators.stream().collect(Collectors.toMap(
                Function.identity(),
                e -> levelsByEducatorId.getOrDefault(e.getId(), List.of()).stream()
                        .map(LevelMapper.INSTANCE::mapToDto).toList()));
    }

    @BatchMapping(typeName = "EducatorResponse", field = "account")
    public Map<EducatorResponse, AccountResponse> account(List<EducatorResponse> educators) {
        List<String> educatorIds = educators.stream().map(EducatorResponse::getId).toList();
        Map<String, Account> accountByEducatorId = accountRepository.findAllByEducatorIds(educatorIds);
        return educators.stream()
                .filter(e -> accountByEducatorId.containsKey(e.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        e -> AccountMapper.INSTANCE.toDto(accountByEducatorId.get(e.getId()))));
    }
}
