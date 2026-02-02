package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.accounts.CreateAccountAndTagToTenantUseCaseImpl;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCaseImpl;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCaseImpl;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCaseImpl;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import com.aura.core.usecases.courses.CreateCourseUseCaseImpl;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCaseImpl;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCaseImpl;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCaseImpl;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCaseImpl;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCaseImpl;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCaseImpl;
import com.aura.core.usecases.onboarding.DefaultOnboardInstitutionUseCaseImpl;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.CreateOutletUseCaseImpl;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCaseImpl;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCaseImpl;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCaseImpl;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.CreateTopicUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    OnboardInstitutionUseCase onboardInstitutionUseCase(InstitutionRepository institutionRepository, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
        return new DefaultOnboardInstitutionUseCaseImpl(institutionRepository, identityService, tenantInitialDataLoader);
    }

    @Bean
    CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository, IdentityService identityService) {
        return new CreateAccountAndTagToTenantUseCaseImpl(accountRepository, identityService);
    }

    @Bean
    CreateParentAccountUseCase createParentAccountUseCase(CreateAccountAndTagToTenantUseCaseImpl createAccountAndTagToTenantUseCase, AccountRepository accountRepository, IdentityService identityService) {
        return new CreateParentAccountUseCaseImpl(createAccountAndTagToTenantUseCase, accountRepository, identityService);
    }

    @Bean
    CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase(StudentRepository studentRepository, AccountRepository accountRepository) {
        return new CreateStudentsInParentAccountUseCaseImpl(studentRepository, accountRepository);
    }

    @Bean
    CreateOutletUseCase createOutletUseCase(OutletRepository outletRepository) {
        return new CreateOutletUseCaseImpl(outletRepository);
    }

    @Bean
    OutletQueryUseCase outletQueryUseCase(OutletRepository outletRepository) {
        return new OutletQueryUseCaseImpl(outletRepository);
    }

    @Bean
    CreateCourseUseCase createCourseUseCase(CourseRepository courseRepository, OutletRepository outletRepository) {
        return new CreateCourseUseCaseImpl(courseRepository, outletRepository);
    }

    @Bean
    CreateLessonUseCase createLessonUseCase(LessonRepository lessonRepository, OutletRepository outletRepository) {
        return new CreateLessonUseCaseImpl(lessonRepository, outletRepository);
    }

    @Bean
    TagStudentToSchoolUseCase tagStudentToSchoolUseCase(StudentRepository studentRepository) {
        return new TagStudentToSchoolUseCaseImpl(studentRepository);
    }

    @Bean
    TagStudentToLevelUseCase tagStudentToLevelUseCase(StudentRepository studentRepository) {
        return new TagStudentToLevelUseCaseImpl(studentRepository);
    }

    @Bean
    EnrollStudentToCourseUseCase enrollStudentToCourseUseCase(StudentRepository studentRepository, CourseRepository courseRepository) {
        return new EnrollStudentToCourseUseCaseImpl(studentRepository, courseRepository);
    }

    @Bean
    CreateEducatorInAccountUseCase createEducatorInAccountUseCase(AccountRepository accountRepository, EducatorRepository educatorRepository, IdentityService identityService) {
        return new CreateEducatorInAccountUseCaseImpl(accountRepository, educatorRepository, identityService);
    }

    @Bean
    EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase(EducatorRepository educatorRepository) {
        return new EnrollEducatorToOutletUseCaseImpl(educatorRepository);
    }

    @Bean
    TagEducatorToCourseUseCase tagEducatorToCourseUseCase(EducatorRepository educatorRepository) {
        return new TagEducatorToCourseUseCaseImpl(educatorRepository);
    }

    @Bean
    TagEducatorToLevelUseCase tagEducatorToLevelUseCase(EducatorRepository educatorRepository) {
        return new TagEducatorToLevelUseCaseImpl(educatorRepository);
    }

    @Bean
    TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase(EducatorRepository educatorRepository) {
        return new TagEducatorToSubjectUseCaseImpl(educatorRepository);
    }


    @Bean
    CreateTopicUseCase createTopicUseCase(TopicRepository topicRepository) {
        return new CreateTopicUseCaseImpl(topicRepository);
    }
}
