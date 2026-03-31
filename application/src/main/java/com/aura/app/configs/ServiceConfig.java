package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.core.usecases.institutions.InstitutionQueryUseCaseImpl;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.LinkLessonsToCourseUseCase;
import com.aura.core.usecases.levels.LevelQueryUseCase;
import com.aura.core.usecases.levels.LevelQueryUseCaseImpl;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.details.institutions.onboarding.TenantInitialDataLoaderImpl;
import com.aura.details.services.AccountService;
import com.aura.details.services.CourseService;
import com.aura.details.services.LessonService;
import com.aura.details.services.OnboardingService;
import com.aura.details.services.OutletService;
import com.aura.details.services.SubjectService;
import com.aura.details.services.TopicService;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelTemplateRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLevelLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectLevelLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicSubjectLinkTemplateRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicTemplateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    TenantInitialDataLoader tenantInitialDataLoader(JpaSchoolTemplateRepository jpaSchoolTemplateRepository, JpaSchoolRepository jpaSchoolRepository, JpaLevelTemplateRepository jpaLevelTemplateRepository, JpaLevelRepository jpaLevelRepository, JpaSubjectTemplateRepository jpaSubjectTemplateRepository, JpaSubjectRepository jpaSubjectRepository, JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository, JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository, JpaTopicTemplateRepository jpaTopicTemplateRepository, JpaTopicRepository jpaTopicRepository, JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository, JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository) {
        return new TenantInitialDataLoaderImpl(jpaSchoolTemplateRepository, jpaSchoolRepository, jpaLevelTemplateRepository, jpaLevelRepository, jpaSubjectTemplateRepository, jpaSubjectRepository, jpaSubjectLevelLinkTemplateRepository, jpaSubjectLevelLinkRepository, jpaTopicTemplateRepository, jpaTopicRepository, jpaTopicSubjectLinkTemplateRepository, jpaTopicSubjectLinkRepository);
    }

    @Bean
    OnboardingService onboardingService(OnboardInstitutionUseCase onboardInstitutionUseCase) {
        return new OnboardingService(onboardInstitutionUseCase);
    }

    @Bean
    InstitutionQueryUseCase institutionQueryService(InstitutionRepository institutionRepository) {
        return new InstitutionQueryUseCaseImpl(institutionRepository);
    }

    @Bean
    LevelQueryUseCase levelQueryService(LevelRepository levelRepository) {
        return new LevelQueryUseCaseImpl(levelRepository);
    }

    @Bean
    AccountService accountService(CreateAccountUseCase createAccountUseCase,
                                  CreateParentAccountUseCase createParentAccountUseCase,
                                  CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase,
                                  TagStudentToLevelUseCase tagStudentToLevelUseCase,
                                  TagStudentToSchoolUseCase tagStudentToSchoolUseCase,
                                  EnrollStudentToCourseUseCase enrollStudentToCourseUseCase,
                                  CreateEducatorInAccountUseCase createEducatorInAccountUseCase,
                                  TagEducatorToCourseUseCase tagEducatorToCourseUseCase,
                                  TagEducatorToLevelUseCase tagEducatorToLevelUseCase,
                                  TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase,
                                  EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase,
                                  AccountRepository accountRepository,
                                  IdentityService identityService) {
        return new AccountService(createAccountUseCase,
                createParentAccountUseCase,
                createStudentsInParentAccountUseCase,
                tagStudentToLevelUseCase,
                tagStudentToSchoolUseCase,
                enrollStudentToCourseUseCase,
                createEducatorInAccountUseCase,
                tagEducatorToCourseUseCase,
                tagEducatorToLevelUseCase,
                tagEducatorToSubjectUseCase,
                enrollEducatorToOutletUseCase, accountRepository, identityService);
    }

    @Bean
    OutletService outletService(CreateOutletUseCase createOutletUseCase, OutletQueryUseCase outletQueryUseCase) {
        return new OutletService(createOutletUseCase, outletQueryUseCase);
    }

    @Bean
    CourseService courseService(CreateCourseUseCase createCourseUseCase) {
        return new CourseService(createCourseUseCase);
    }

    @Bean
    LessonService lessonService(CreateLessonUseCase createLessonUseCase, AddEducatorsToLessonUseCase addEducatorsToLessonUseCase, LinkLessonsToCourseUseCase linkLessonsToCourseUseCase) {
        return new LessonService(createLessonUseCase, addEducatorsToLessonUseCase, linkLessonsToCourseUseCase);
    }

    @Bean
    TopicService topicService(CreateTopicUseCase createTopicUseCase) {
        return new TopicService(createTopicUseCase);
    }

    @Bean
    SubjectService subjectService(CreateSubjectUseCase createSubjectUseCase, CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase) {
        return new SubjectService(createSubjectUseCase, createTopicSubjectLinksUseCase);
    }
}
