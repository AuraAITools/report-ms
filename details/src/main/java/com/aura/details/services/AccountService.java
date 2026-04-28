package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.usecases.accounts.AssignUserRoleUseCase;
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
import lombok.RequiredArgsConstructor;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
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
    private final RelationshipAuthorizationService relationshipService;
    private final AssignUserRoleUseCase assignUserRoleUseCase;

    @Transactional
    public void assignUserRole(String email, String resourceType, String resourceId, String role) {
        assignUserRoleUseCase.assignRole(email, resourceType, resourceId, role);
    }

    @Transactional
    public Account createAccountWithInstitutionAdminRole(Account account) {
        Account createdAccount = createAccountUseCase.createAccount(account);
        relationshipService.writeRelationship("institution", account.getTenantId(),
                "admin", "user", createdAccount.getUserId());
        return createdAccount;
    }

    @Transactional
    public Account createParentAccount(Account account) {
        Account created = createParentAccountUseCase.createParentAccount(account);
        relationshipService.writeRelationship(
                "institution", created.getTenantId(), "parent_user", "user", created.getUserId());
        return created;
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
        relationshipService.writeRelationship(
                "institution", account.getTenantId(), "educator", "user", account.getUserId());
        List<Level> taggedLevels = params.levelIds().stream().map(levelId -> tagEducatorToLevelUseCase.tag(createdEducator.getId(), levelId)).toList();
        List<Course> taggedCourses = params.courseIds().stream().map(courseId -> tagEducatorToCourseUseCase.tag(createdEducator.getId(), courseId)).toList();
        List<Subject> taggedSubjects = params.subjectIds().stream().map(sid -> tagEducatorToSubjectUseCase.tag(createdEducator.getId(), sid)).toList();
        List<Outlet> enrolledOutlets = params.outletIds().stream().map(oid -> enrollEducatorToOutletUseCase.enroll(createdEducator.getId(), oid)).toList();
        return CreatedEducatorResult.builder()
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
