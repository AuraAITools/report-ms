package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.usecases.accounts.AssignInstitutionAdminRoleUseCase;
import com.aura.core.usecases.accounts.AssignParentRoleUseCase;
import com.aura.core.usecases.accounts.AssignRoleUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import lombok.Builder;


import static java.util.Optional.ofNullable;

public class AccountService {
    private final CreateAccountUseCase createAccountUseCase;
    private final CreateParentAccountUseCase createParentAccountUseCase;
    private final CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase;
    private final TagStudentToLevelUseCase tagStudentToLevelUseCase;
    private final TagStudentToSchoolUseCase tagStudentToSchoolUseCase;
    private final EnrollStudentToCourseUseCase enrollStudentToCourseUseCase;
    private final CreateEducatorInAccountUseCase createEducatorInAccountUseCase;
    private final TagEducatorToCourseUseCase tagEducatorToCourseUseCase;
    private final TagEducatorToLevelUseCase tagEducatorToLevelUseCase;
    private final TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase;
    private final EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase;
    private final AccountRepository accountRepository;
    private final IdentityService identityService;

    public AccountService(CreateAccountUseCase createAccountUseCase, CreateParentAccountUseCase createParentAccountUseCase, CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase, TagStudentToLevelUseCase tagStudentToLevelUseCase, TagStudentToSchoolUseCase tagStudentToSchoolUseCase, EnrollStudentToCourseUseCase enrollStudentToCourseUseCase, CreateEducatorInAccountUseCase createEducatorInAccountUseCase, TagEducatorToCourseUseCase tagEducatorToCourseUseCase, TagEducatorToLevelUseCase tagEducatorToLevelUseCase, TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase, EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase, AccountRepository accountRepository, IdentityService identityService) {
        this.createAccountUseCase = createAccountUseCase;
        this.createParentAccountUseCase = createParentAccountUseCase;
        this.createStudentsInParentAccountUseCase = createStudentsInParentAccountUseCase;
        this.tagStudentToLevelUseCase = tagStudentToLevelUseCase;
        this.tagStudentToSchoolUseCase = tagStudentToSchoolUseCase;
        this.enrollStudentToCourseUseCase = enrollStudentToCourseUseCase;
        this.createEducatorInAccountUseCase = createEducatorInAccountUseCase;
        this.tagEducatorToCourseUseCase = tagEducatorToCourseUseCase;
        this.tagEducatorToLevelUseCase = tagEducatorToLevelUseCase;
        this.tagEducatorToSubjectUseCase = tagEducatorToSubjectUseCase;
        this.enrollEducatorToOutletUseCase = enrollEducatorToOutletUseCase;
        this.accountRepository = accountRepository;
        this.identityService = identityService;
    }

    @Transactional
    public Account createAccountWithInstitutionAdminRole(Account account) {
        Account createdAccount = createAccountUseCase.createAccount(account);
        AssignRoleUseCase assignRoleUseCase = new AssignInstitutionAdminRoleUseCase(identityService, createdAccount.getTenantId(), createdAccount.getUserId());
        assignRoleUseCase.assignRole();
        return createdAccount;
    }

    @Transactional
    public Account createParentAccount(Account account) {
        Account createdAccount = createParentAccountUseCase.createParentAccount(account);
        AssignRoleUseCase assignRoleUseCase = new AssignParentRoleUseCase(identityService, createdAccount.getTenantId(), createdAccount.getUserId());
        assignRoleUseCase.assignRole();
        return createdAccount;
    }

    @Transactional
    public List<CreatedStudentResult> addStudentsToParentAccount(List<CreateStudentAccountParams> createStudentAccountParams) {
        return createStudentAccountParams.stream().map(this::createStudentAndLinkToParentAccount).toList();
    }

    @Transactional
    public CreatedStudentResult createStudentAndLinkToParentAccount(CreateStudentAccountParams createStudentAccountParams) {
        Student student = createStudentsInParentAccountUseCase.createStudent(createStudentAccountParams.accountId, createStudentAccountParams.student);
        Level level = tagStudentToLevelUseCase.tagStudentToLevel(student.getId(), createStudentAccountParams.levelId());
        School school = tagStudentToSchoolUseCase.tagStudentToSchool(student.getId(), createStudentAccountParams.schoolId());


        var courses = createStudentAccountParams.studentCourseEnrollmentParams().stream()
                .map(param -> enrollStudentToCourseUseCase.enroll(student.getId(), param.courseId(), createStudentAccountParams.tenantId()))
                .toList();
        return CreatedStudentResult
                .builder()
                .student(student)
                .level(level)
                .courses(courses)
                .school(school)
                .build();
    }

    @Transactional
    public CreatedEducatorResult createAndLinkEducatorToAccount(CreateEducatorAccountParams params) {
        Educator createdEducator = createEducatorInAccountUseCase.createEducator(params.accountId(), params.educator());
        Account account = ofNullable(accountRepository.findById(params.accountId())).orElseThrow(() -> new RuntimeException("account not found"));
        List<Level> taggedLevels = params.levelIds().stream().map(levelId -> tagEducatorToLevelUseCase.tag(createdEducator.getId(), levelId)).toList();
        List<Course> taggedCourses = params.courseIds().stream().map(courseId -> tagEducatorToCourseUseCase.tag(createdEducator.getId(), courseId)).toList();
        List<Subject> taggedSubjects = params.subjectIds().stream().map(sid -> tagEducatorToSubjectUseCase.tag(createdEducator.getId(), sid)).toList();
        List<Outlet> enrolledOutlets = params.outletIds().stream().map(oid -> enrollEducatorToOutletUseCase.enroll(createdEducator.getId(), oid)).toList();
        return CreatedEducatorResult.builder()
                // missing account
                .account(account)
                .educator(createdEducator)
                .courses(taggedCourses)
                .levels(taggedLevels)
                .subjects(taggedSubjects)
                .outlets(enrolledOutlets)
                .build();
    }

    @Transactional
    public List<CreatedEducatorResult> createAndLinkEducatorsToAccount(List<CreateEducatorAccountParams> createEducatorAccountParams) {
        return createEducatorAccountParams.stream().map(this::createAndLinkEducatorToAccount).toList();
    }

    @Builder
    public record CreatedEducatorResult(Educator educator, List<Course> courses, List<Subject> subjects,
                                        List<Outlet> outlets, List<Level> levels, Account account) {
    }

    @Builder
    public record CreateEducatorAccountParams(Educator educator, List<String> courseIds, List<String> subjectIds,
                                              List<String> outletIds, List<String> levelIds, String accountId) {
    }


    @Builder
    public record CreateStudentAccountParams(Student student, String levelId, String schoolId, String accountId,
                                             String tenantId,
                                             List<CreateStudentCourseEnrollmentParams> studentCourseEnrollmentParams) {
    }

    @Builder
    public record CreateStudentCourseEnrollmentParams(String courseId, Instant enrollmentDateTime) {
    }

    @Builder
    public record CreatedStudentResult(Student student, Level level, School school, List<Course> courses) {
    }
}
