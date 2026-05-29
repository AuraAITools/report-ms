package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeature;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.events.EducatorAccountCreatedEvent;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.usecases.accounts.AccountQueryUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
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
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;


import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private static final List<String> ACCOUNT_REQUIRED_ACTIONS = List.of("VERIFY_EMAIL", "UPDATE_PASSWORD");

    private final CreateAccountUseCase createAccountUseCase;
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
    private final ApplicationEventPublisher applicationEventPublisher;
    private final String actionRedirectUri;
    private final ParentRepository parentRepository;
    private final AccountQueryUseCase accountQueryUseCase;

    @Transactional
    public @Nullable Account findById(String id) {
        Account account = accountQueryUseCase.findById(id);
        return account == null ? null : reconcileVerifyingState(account);
    }

    @Transactional
    public List<Account> findAllByTenantId(String tenantId) {
        return accountQueryUseCase.findAllByTenantId(tenantId).stream()
                .map(this::reconcileVerifyingState)
                .toList();
    }

    /**
     * If the account is still VERIFYING, ask the IdP whether the user has
     * completed every required action we asked of them at signup
     * (VERIFY_EMAIL, UPDATE_PASSWORD). Promote to ACTIVE only when none of
     * those actions remain pending — Keycloak's {@code enabled} flag is set
     * at creation and would flip us to ACTIVE prematurely.
     */
    private Account reconcileVerifyingState(Account account) {
        if (Account.STATE.VERIFYING == account.getState()
                && identityService.hasCompletedRequiredActions(account.getUserId(), ACCOUNT_REQUIRED_ACTIONS)) {
            accountRepository.updateStateById(account.getId(), Account.STATE.ACTIVE);
            account.setState(Account.STATE.ACTIVE);
        }
        return account;
    }

    public Map<String, List<Student>> findStudentsByAccountIds(List<String> accountIds) {
        return accountQueryUseCase.findStudentsByAccountIds(accountIds);
    }

    public Map<String, List<Educator>> findEducatorsByAccountIds(List<String> accountIds) {
        return accountQueryUseCase.findEducatorsByAccountIds(accountIds);
    }

    public Map<String, Parent> findParentsByAccountIds(List<String> accountIds) {
        return accountQueryUseCase.findParentsByAccountIds(accountIds);
    }

    @Transactional
    public Account createAccount(Account account) {
        Account createdAccount = createAccountUseCase.createAccount(account);
        List<RelationshipAuthorizationService.Relationship> relationships = List.of(
                new RelationshipAuthorizationService.Relationship("account", createdAccount.getId(), "parent", "institution", account.getTenantId()),
                new RelationshipAuthorizationService.Relationship("account_user_group", createdAccount.getId(),
                        "group_admin", "user", createdAccount.getUserId()),
                new RelationshipAuthorizationService.Relationship("account_user_group", createdAccount.getId(),
                        "admin", "user", createdAccount.getUserId()),
                new RelationshipAuthorizationService.Relationship("account_user_group", createdAccount.getId(), "parent", "system_user_group", "aura")
        );
        relationshipService.writeRelationships(relationships);
        // TODO: stop sending email if repeated account is created for same email cos idp is already verified
        Thread.startVirtualThread(() -> {
            try {
                log.info("Sending actions email to userId={}", createdAccount.getUserId());
                identityService.sendActionsEmail(createdAccount.getUserId(), ACCOUNT_REQUIRED_ACTIONS, actionRedirectUri);
            } catch (Exception e) {
                log.error("Failed to send actions email", e);
            }
        });
        return createdAccount;
    }

    @Transactional
    public List<CreatedStudentResult> createStudentsInAccount(List<CreateStudentParams> createStudentParams) {
        return createStudentParams.stream().map(this::createStudentInAccount).toList();
    }

    @Transactional
    public CreatedStudentResult createStudentInAccount(CreateStudentParams createStudentParams) {
        Student student = createStudentsInParentAccountUseCase.createStudent(createStudentParams.accountId, createStudentParams.student);
        Level level = tagStudentToLevelUseCase.tagStudentToLevel(student.getId(), createStudentParams.levelId());
        School school = tagStudentToSchoolUseCase.tagStudentToSchool(student.getId(), createStudentParams.schoolId());

        var courses = createStudentParams.studentCourseEnrollmentParams().stream()
                .map(param -> enrollStudentToCourseUseCase.enroll(student.getId(), param.courseId(), createStudentParams.tenantId()))
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
    public CreatedEducatorResult createEducatorInAccount(CreateEducatorAccountParams params) {
        Educator createdEducator = createEducatorInAccountUseCase.createEducator(params.accountId(), params.educator());
        Account account = ofNullable(accountRepository.findById(params.accountId())).orElseThrow(() -> new RuntimeException("account not found"));
        List<Level> taggedLevels = params.levelIds().stream().map(levelId -> tagEducatorToLevelUseCase.tag(createdEducator.getId(), levelId)).toList();
        List<Course> taggedCourses = params.courseIds().stream().map(courseId -> tagEducatorToCourseUseCase.tag(createdEducator.getId(), courseId)).toList();
        List<Subject> taggedSubjects = params.subjectIds().stream().map(sid -> tagEducatorToSubjectUseCase.tag(createdEducator.getId(), sid)).toList();
        List<Outlet> enrolledOutlets = params.outletIds().stream().map(oid -> enrollEducatorToOutletUseCase.enroll(createdEducator.getId(), oid)).toList();
        identityService.sendActionsEmail(account.getUserId(), ACCOUNT_REQUIRED_ACTIONS, actionRedirectUri);
        applicationEventPublisher.publishEvent(new EducatorAccountCreatedEvent(account));
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
    public List<CreatedEducatorResult> createEducatorsInAccount(List<CreateEducatorAccountParams> createEducatorAccountParams) {
        return createEducatorAccountParams.stream().map(this::createEducatorInAccount).toList();
    }

    @Transactional
    public Account enableAccountFeature(String accountId, AccountFeature feature) {
        accountRepository.enableFeature(accountId, feature);
        return ofNullable(accountRepository.findById(accountId))
                .orElseThrow(() -> new RuntimeException("account does not exist"));
    }

    @Transactional
    public ParentAndStudentsResult createParentAndStudentsInAccount(String accountId, Parent parent, List<CreateStudentParams> studentParams) {
        Parent createdParent = createParentInAccount(accountId, parent);
        List<CreatedStudentResult> createdStudents = createStudentsInAccount(studentParams);
        return new ParentAndStudentsResult(createdParent, createdStudents);
    }

    @Transactional
    public Parent createParentInAccount(String accountId, Parent parent) {
        return Optional.ofNullable(accountRepository.findById(accountId))
                .map(acc -> {
                    if (acc.getParent() != null) {
                        throw new RuntimeException("parent already exists");
                    } // TODO: should make this a unique constraint to prevent race conditions
                    parent.setAccountId(acc.getId());
                    return parentRepository.save(parent);
                })
                .orElseThrow(() -> new RuntimeException("account does not exist"));
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
    public record CreateStudentParams(Student student, String levelId, String schoolId, String accountId,
                                      String tenantId,
                                      List<CreateStudentCourseEnrollmentParams> studentCourseEnrollmentParams) {
    }

    @Builder
    public record CreateStudentCourseEnrollmentParams(String courseId, Instant enrollmentDateTime) {
    }

    @Builder
    public record CreatedStudentResult(Student student, Level level, School school, List<Course> courses) {
    }

    public record ParentAndStudentsResult(Parent parent, List<CreatedStudentResult> students) {
    }
}
