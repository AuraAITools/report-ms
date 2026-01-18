package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.usecases.accounts.AssignInstitutionAdminRoleUseCase;
import com.aura.core.usecases.accounts.AssignParentRoleUseCase;
import com.aura.core.usecases.accounts.AssignRoleUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import lombok.Builder;

public class AccountService {
    private final CreateAccountUseCase createAccountUseCase;
    private final CreateParentAccountUseCase createParentAccountUseCase;
    private final CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase;
    private final TagStudentToLevelUseCase tagStudentToLevelUseCase;
    private final TagStudentToSchoolUseCase tagStudentToSchoolUseCase;
    private final EnrollStudentToCourseUseCase enrollStudentToCourseUseCase;
    private final IdentityService identityService;

    public AccountService(CreateAccountUseCase createAccountUseCase, CreateParentAccountUseCase createParentAccountUseCase, CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase, TagStudentToLevelUseCase tagStudentToLevelUseCase, TagStudentToSchoolUseCase tagStudentToSchoolUseCase, EnrollStudentToCourseUseCase enrollStudentToCourseUseCase, IdentityService identityService) {
        this.createAccountUseCase = createAccountUseCase;
        this.createParentAccountUseCase = createParentAccountUseCase;
        this.createStudentsInParentAccountUseCase = createStudentsInParentAccountUseCase;
        this.tagStudentToLevelUseCase = tagStudentToLevelUseCase;
        this.tagStudentToSchoolUseCase = tagStudentToSchoolUseCase;
        this.enrollStudentToCourseUseCase = enrollStudentToCourseUseCase;
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
        return createStudentAccountParams.stream().map(this::addStudentToParentAccount).toList();
    }

    @Transactional
    public CreatedStudentResult addStudentToParentAccount(CreateStudentAccountParams createStudentAccountParams) {
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
