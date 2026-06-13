package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeature;
import com.aura.core.entities.accounts.AccountFeatureState;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.events.EducatorAccountCreatedEvent;
import com.aura.core.events.ParentAccountCreatedEvent;
import com.aura.core.exceptions.DuplicateResourceException;
import com.aura.core.exceptions.ResourceNotFoundException;
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
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final Duration PROFILE_IMAGE_UPLOAD_TTL = Duration.ofMinutes(5);
    private static final Pattern PROFILE_IMAGE_KEY_PATTERN =
            Pattern.compile("^institutions/([^/]+)/accounts/([^/]+)/profile\\.(png|jpg|webp)$");

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
    private final AssetUrlService assetUrlService;
    private final ProfileImageValidator profileImageValidator;

    @Transactional
    public @Nullable Account findById(String id) {
        Account account = accountQueryUseCase.findById(id);
        return account == null ? null : reconcileVerifyingState(account);
    }

    @Transactional
    public @Nullable Account findByUserIdAndTenantId(String userId, String tenantId) {
        Account account = accountQueryUseCase.findByUserIdAndTenantId(userId, tenantId);
        return account == null ? null : reconcileVerifyingState(account);
    }

    @Transactional
    public List<Account> findAllByTenantId(String tenantId) {
        return accountQueryUseCase.findAllByTenantId(tenantId).stream()
                .map(this::reconcileVerifyingState)
                .toList();
    }

    /**
     * Batch-loads accounts for the given userIds within the tenant, keyed by userId.
     * Used by AccountMinimalDetailGqlController to resolve audit-field projections.
     */
    @Transactional
    public Map<String, Account> findAllByUserIdsAndTenantId(List<String> userIds, String tenantId) {
        return accountRepository.findAllByUserIdsAndTenantId(userIds, tenantId);
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
                new RelationshipAuthorizationService.Relationship("account_user_group", createdAccount.getId(), "parent", "institution_user_group", account.getTenantId())
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
        Account account = ofNullable(accountRepository.findById(params.accountId()))
                .orElseThrow(() -> new ResourceNotFoundException("Account", params.accountId()));
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
    public Account updateAccountFeatureState(String accountId, AccountFeature feature, AccountFeatureState state) {
        accountRepository.updateFeatureState(accountId, feature, state);
        return ofNullable(accountRepository.findById(accountId))
                .orElseThrow(() -> new ResourceNotFoundException("Account", accountId));
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
                        throw new DuplicateResourceException("parent already exists for account " + accountId);
                    } // TODO: should make this a unique constraint to prevent race conditions
                    parent.setAccountId(acc.getId());
                    Parent createdParent = parentRepository.save(parent);
                    applicationEventPublisher.publishEvent(new ParentAccountCreatedEvent(acc));
                    return createdParent;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Account", accountId));
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

    public PresignedProfileImageUpload requestProfileImageUpload(
            String institutionId, String accountId, String contentType) {
        if (!ProfileImageValidator.ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ProfileImageValidator.ProfileImageRejectedException(
                    "content type '" + contentType + "' is not allowed; must be one of "
                            + ProfileImageValidator.ALLOWED_CONTENT_TYPES);
        }
        String ext = ProfileImageValidator.extensionFor(contentType);
        String fileKey = "institutions/" + institutionId + "/accounts/" + accountId + "/profile." + ext;
        String uploadUrl = assetUrlService.presignPut(fileKey, contentType, PROFILE_IMAGE_UPLOAD_TTL);
        return new PresignedProfileImageUpload(uploadUrl, fileKey);
    }

    @Transactional
    public Account confirmProfileImageUpload(String institutionId, String accountId, String fileKey) {
        Matcher matcher = PROFILE_IMAGE_KEY_PATTERN.matcher(fileKey);
        if (!matcher.matches()
                || !matcher.group(1).equals(institutionId)
                || !matcher.group(2).equals(accountId)) {
            throw new IllegalArgumentException("fileKey does not match expected scheme for this account: " + fileKey);
        }
        Account account = accountQueryUseCase.findById(accountId);
        if (account == null || !institutionId.equals(account.getTenantId())) {
            throw new IllegalArgumentException("account " + accountId + " not in institution " + institutionId);
        }
        if (!assetUrlService.objectExists(fileKey)) {
            throw new IllegalStateException("uploaded file not found at key " + fileKey);
        }
        String actualContentType = assetUrlService.getObjectContentType(fileKey);
        long sizeBytes = assetUrlService.getObjectSizeBytes(fileKey);
        byte[] bytes = assetUrlService.getObjectBytes(fileKey);
        try {
            profileImageValidator.validate(bytes, actualContentType, sizeBytes);
        } catch (ProfileImageValidator.ProfileImageRejectedException e) {
            assetUrlService.deleteObject(fileKey);
            throw e;
        }
        String previousKey = account.getProfileImageKey();
        if (previousKey != null && !previousKey.equals(fileKey)) {
            assetUrlService.deleteObject(previousKey);
        }
        accountRepository.updateProfileImageKeyById(accountId, fileKey);
        // The bulk update bypasses the persistence context, so a re-fetch inside this transaction
        // would return the stale first-level-cached model with the old key. Carry the new key on
        // the already-loaded domain entity instead.
        account.setProfileImageKey(fileKey);
        return account;
    }

    public record PresignedProfileImageUpload(String uploadUrl, String fileKey) {
    }
}
