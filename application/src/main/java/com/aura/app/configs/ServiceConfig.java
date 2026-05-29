package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonPlanTopicLinkRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.repositories.SchoolRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.accounts.AccountQueryUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.authorization.GrantUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.LookupPermissionsUseCase;
import com.aura.core.usecases.authorization.RevokeUserGroupRoleUseCase;
import com.aura.core.usecases.courses.CourseQueryUseCase;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import com.aura.core.usecases.courses.DeleteCourseUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.educators.DeleteEducatorUseCase;
import com.aura.core.usecases.educators.EducatorQueryUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.educators.UpdateEducatorUseCase;
import com.aura.core.usecases.email.SendEmailUseCase;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.core.usecases.institutions.InstitutionQueryUseCaseImpl;
import com.aura.core.usecases.lesson_objectives.AddTopicsToLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.CreateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.DeleteLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.LessonObjectiveQueryUseCase;
import com.aura.core.usecases.lesson_objectives.UpdateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_plans.AddMaterialsToLessonPlanUseCase;
import com.aura.core.usecases.lesson_plans.AddStudentsToLessonPlanUseCase;
import com.aura.core.usecases.lesson_plans.AddTopicsToLessonPlanUseCase;
import com.aura.core.usecases.lesson_plans.CreateLessonPlanUseCase;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import com.aura.core.usecases.levels.LevelQueryUseCase;
import com.aura.core.usecases.levels.LevelQueryUseCaseImpl;
import com.aura.core.usecases.materials.CreateMaterialInInstitutionUseCase;
import com.aura.core.usecases.materials.DeleteMaterialUseCase;
import com.aura.core.usecases.materials.MaterialQueryUseCase;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import com.aura.core.usecases.outlet_rooms.CreateOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.DeleteOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.OutletRoomQueryUseCase;
import com.aura.core.usecases.outlet_rooms.UpdateOutletRoomUseCase;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.DeleteOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.outlets.UpdateOutletUseCase;
import com.aura.core.usecases.parents.DeleteParentUseCase;
import com.aura.core.usecases.parents.ParentQueryUseCase;
import com.aura.core.usecases.parents.UpdateParentUseCase;
import com.aura.core.usecases.schools.CreateSchoolUseCase;
import com.aura.core.usecases.schools.DeleteSchoolUseCase;
import com.aura.core.usecases.schools.SchoolQueryUseCase;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCase;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import com.aura.core.usecases.subjects.DeleteSubjectUseCase;
import com.aura.core.usecases.subjects.SubjectQueryUseCase;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import com.aura.core.usecases.topics.TopicQueryUseCase;
import com.aura.details.institutions.onboarding.TenantInitialDataLoaderImpl;
import com.aura.details.services.AccountService;
import com.aura.details.services.AssetUrlService;
import com.aura.details.services.AuthorizationService;
import com.aura.details.services.CourseService;
import com.aura.details.services.EducatorService;
import com.aura.details.services.LessonObjectiveService;
import com.aura.details.services.LessonPlanService;
import com.aura.details.services.LessonService;
import com.aura.details.services.LevelService;
import com.aura.details.services.MaterialService;
import com.aura.details.services.OnboardingService;
import com.aura.details.services.OutletRoomService;
import com.aura.details.services.OutletService;
import com.aura.details.services.ParentService;
import com.aura.details.services.SchoolService;
import com.aura.details.services.StudentLessonReportService;
import com.aura.details.services.StudentService;
import com.aura.details.services.SubjectService;
import com.aura.details.services.TopicService;
import com.aura.details.services.email.EmailService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    TenantInitialDataLoader tenantInitialDataLoader(JpaSchoolTemplateRepository jpaSchoolTemplateRepository, JpaSchoolRepository jpaSchoolRepository, JpaLevelTemplateRepository jpaLevelTemplateRepository, JpaLevelRepository jpaLevelRepository, JpaSubjectTemplateRepository jpaSubjectTemplateRepository, JpaSubjectRepository jpaSubjectRepository, JpaSubjectLevelLinkTemplateRepository jpaSubjectLevelLinkTemplateRepository, JpaSubjectLevelLinkRepository jpaSubjectLevelLinkRepository, JpaTopicTemplateRepository jpaTopicTemplateRepository, JpaTopicRepository jpaTopicRepository, JpaTopicSubjectLinkTemplateRepository jpaTopicSubjectLinkTemplateRepository, JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository) {
        return new TenantInitialDataLoaderImpl(jpaSchoolTemplateRepository, jpaSchoolRepository, jpaLevelTemplateRepository, jpaLevelRepository, jpaSubjectTemplateRepository, jpaSubjectRepository, jpaSubjectLevelLinkTemplateRepository, jpaSubjectLevelLinkRepository, jpaTopicTemplateRepository, jpaTopicRepository, jpaTopicSubjectLinkTemplateRepository, jpaTopicSubjectLinkRepository);
    }

    @Bean
    OnboardingService onboardingService(
            OnboardInstitutionUseCase onboardInstitutionUseCase,
            RelationshipAuthorizationService relationshipService) {
        return new OnboardingService(onboardInstitutionUseCase, relationshipService);
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
                                  IdentityService identityService,
                                  RelationshipAuthorizationService relationshipService,
                                  org.springframework.context.ApplicationEventPublisher applicationEventPublisher,
                                  @Value("${aura.email.action-redirect-uri}") String actionRedirectUri,
                                  ParentRepository parentRepository,
                                  AccountQueryUseCase accountQueryUseCase) {
        return new AccountService(createAccountUseCase,
                createStudentsInParentAccountUseCase,
                tagStudentToLevelUseCase,
                tagStudentToSchoolUseCase,
                enrollStudentToCourseUseCase,
                createEducatorInAccountUseCase,
                tagEducatorToCourseUseCase,
                tagEducatorToLevelUseCase,
                tagEducatorToSubjectUseCase,
                enrollEducatorToOutletUseCase,
                accountRepository,
                identityService,
                relationshipService,
                applicationEventPublisher,
                actionRedirectUri,
                parentRepository,
                accountQueryUseCase);
    }

    @Bean
    OutletService outletService(CreateOutletUseCase createOutletUseCase,
                                OutletQueryUseCase outletQueryUseCase,
                                UpdateOutletUseCase updateOutletUseCase,
                                DeleteOutletUseCase deleteOutletUseCase,
                                CourseService courseService,
                                OutletRepository outletRepository,
                                CourseRepository courseRepository,
                                RelationshipAuthorizationService relationshipService) {
        return new OutletService(createOutletUseCase, outletQueryUseCase, updateOutletUseCase, deleteOutletUseCase,
                courseService, outletRepository, courseRepository, relationshipService);
    }

    @Bean
    OutletRoomService outletRoomService(CreateOutletRoomUseCase createOutletRoomUseCase,
                                        UpdateOutletRoomUseCase updateOutletRoomUseCase,
                                        DeleteOutletRoomUseCase deleteOutletRoomUseCase,
                                        OutletRoomQueryUseCase outletRoomQueryUseCase,
                                        OutletRoomRepository outletRoomRepository,
                                        RelationshipAuthorizationService relationshipService) {
        return new OutletRoomService(createOutletRoomUseCase, updateOutletRoomUseCase, deleteOutletRoomUseCase,
                outletRoomQueryUseCase, outletRoomRepository, relationshipService);
    }

    @Bean
    CourseService courseService(CreateCourseUseCase createCourseUseCase,
                                CourseQueryUseCase courseQueryUseCase,
                                DeleteCourseUseCase deleteCourseUseCase,
                                LessonService lessonService,
                                CourseRepository courseRepository,
                                LessonRepository lessonRepository,
                                RelationshipAuthorizationService relationshipService,
                                SubjectRepository subjectRepository,
                                EducatorRepository educatorRepository,
                                StudentRepository studentRepository) {
        return new CourseService(createCourseUseCase, courseQueryUseCase, deleteCourseUseCase,
                lessonService, courseRepository, lessonRepository, relationshipService,
                subjectRepository, educatorRepository, studentRepository);
    }

    @Bean
    LessonService lessonService(CreateLessonUseCase createLessonUseCase,
                                AddEducatorsToLessonUseCase addEducatorsToLessonUseCase,
                                AddStudentsToLessonUseCase addStudentsToLessonUseCase,
                                AddSubjectsToLessonUseCase addSubjectsToLessonUseCase,
                                AddLevelsToLessonUseCase addLevelsToLessonUseCase,
                                AddTopicsToLessonUseCase addTopicsToLessonUseCase,
                                LessonQueryUseCase lessonQueryUseCase,
                                DeleteLessonUseCase deleteLessonUseCase,
                                LessonRepository lessonRepository,
                                LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository,
                                RelationshipAuthorizationService relationshipService,
                                EducatorRepository educatorRepository,
                                StudentRepository studentRepository,
                                LessonPlanService lessonPlanService) {
        return new LessonService(createLessonUseCase, addEducatorsToLessonUseCase,
                addStudentsToLessonUseCase, addSubjectsToLessonUseCase,
                addLevelsToLessonUseCase, addTopicsToLessonUseCase, lessonQueryUseCase,
                deleteLessonUseCase, lessonRepository, lessonOutletRoomBookingRepository,
                relationshipService, educatorRepository, studentRepository,
                lessonPlanService);
    }

    @Bean
    LessonPlanService lessonPlanService(CreateLessonPlanUseCase createLessonPlanUseCase,
                                        AddTopicsToLessonPlanUseCase addTopicsToLessonPlanUseCase,
                                        AddMaterialsToLessonPlanUseCase addMaterialsToLessonPlanUseCase,
                                        AddStudentsToLessonPlanUseCase addStudentsToLessonPlanUseCase,
                                        LessonPlanRepository lessonPlanRepository,
                                        LessonPlanTopicLinkRepository lessonPlanTopicLinkRepository,
                                        TopicLessonLinkRepository topicLessonLinkRepository,
                                        LessonObjectiveService lessonObjectiveService,
                                        StudentLessonReportService studentLessonReportService) {
        return new LessonPlanService(createLessonPlanUseCase, addTopicsToLessonPlanUseCase,
                addMaterialsToLessonPlanUseCase, addStudentsToLessonPlanUseCase,
                lessonPlanRepository, lessonPlanTopicLinkRepository, topicLessonLinkRepository,
                lessonObjectiveService, studentLessonReportService);
    }

    @Bean
    StudentLessonReportService studentLessonReportService(CreateStudentLessonReportUseCase createStudentLessonReportUseCase,
                                                          StudentLessonLinkRepository studentLessonLinkRepository) {
        return new StudentLessonReportService(createStudentLessonReportUseCase, studentLessonLinkRepository);
    }

    @Bean
    LevelService levelService(CreateLevelUseCase createLevelUseCase,
                              DeleteLevelUseCase deleteLevelUseCase,
                              LevelRepository levelRepository,
                              CourseRepository courseRepository,
                              RelationshipAuthorizationService relationshipService) {
        return new LevelService(createLevelUseCase, deleteLevelUseCase, levelRepository, courseRepository, relationshipService);
    }

    @Bean
    TopicService topicService(
            CreateTopicUseCase createTopicUseCase,
            TopicQueryUseCase topicQueryUseCase,
            DeleteTopicUseCase deleteTopicUseCase,
            TopicRepository topicRepository,
            RelationshipAuthorizationService relationshipService) {
        return new TopicService(createTopicUseCase, topicQueryUseCase, deleteTopicUseCase, topicRepository, relationshipService);
    }

    @Bean
    SubjectService subjectService(
            CreateSubjectUseCase createSubjectUseCase,
            CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase,
            SubjectQueryUseCase subjectQueryUseCase,
            DeleteSubjectUseCase deleteSubjectUseCase,
            SubjectRepository subjectRepository,
            RelationshipAuthorizationService relationshipService) {
        return new SubjectService(createSubjectUseCase, createTopicSubjectLinksUseCase, subjectQueryUseCase,
                deleteSubjectUseCase, subjectRepository, relationshipService);
    }

    @Bean
    SchoolService schoolService(SchoolQueryUseCase schoolQueryUseCase,
                                CreateSchoolUseCase createSchoolUseCase,
                                DeleteSchoolUseCase deleteSchoolUseCase,
                                SchoolRepository schoolRepository) {
        return new SchoolService(schoolQueryUseCase, createSchoolUseCase, deleteSchoolUseCase, schoolRepository);
    }

    @Bean
    StudentService studentService(StudentQueryUseCase studentQueryUseCase,
                                  UpdateStudentUseCase updateStudentUseCase,
                                  DeleteStudentUseCase deleteStudentUseCase,
                                  StudentRepository studentRepository) {
        return new StudentService(studentQueryUseCase, updateStudentUseCase, deleteStudentUseCase, studentRepository);
    }

    @Bean
    EducatorService educatorService(EducatorQueryUseCase educatorQueryUseCase,
                                    UpdateEducatorUseCase updateEducatorUseCase,
                                    DeleteEducatorUseCase deleteEducatorUseCase,
                                    EducatorRepository educatorRepository) {
        return new EducatorService(educatorQueryUseCase, updateEducatorUseCase, deleteEducatorUseCase, educatorRepository);
    }

    @Bean
    ParentService parentService(ParentQueryUseCase parentQueryUseCase,
                                UpdateParentUseCase updateParentUseCase,
                                DeleteParentUseCase deleteParentUseCase) {
        return new ParentService(parentQueryUseCase, updateParentUseCase, deleteParentUseCase);
    }

    @Bean
    AuthorizationService authorizationService(LookupPermissionsUseCase lookupPermissionsUseCase,
                                              GrantUserGroupRoleUseCase grantUserGroupRoleUseCase,
                                              RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase,
                                              OutletRepository outletRepository,
                                              AccountRepository accountRepository,
                                              org.springframework.context.ApplicationEventPublisher applicationEventPublisher) {
        return new AuthorizationService(lookupPermissionsUseCase, grantUserGroupRoleUseCase, revokeUserGroupRoleUseCase,
                outletRepository, accountRepository, applicationEventPublisher);
    }

    @Bean
    EmailService emailService(SendEmailUseCase sendEmailUseCase,
                              AccountRepository accountRepository,
                              @Value("${aura.email.base-url-app}") String baseUrlApp) {
        return new EmailService(sendEmailUseCase, accountRepository, baseUrlApp);
    }

    @Bean
    MaterialService materialService(CreateMaterialInInstitutionUseCase createMaterialInInstitutionUseCase,
                                    MaterialQueryUseCase materialQueryUseCase,
                                    DeleteMaterialUseCase deleteMaterialUseCase,
                                    MaterialRepository materialRepository,
                                    AssetUrlService assetUrlService,
                                    RelationshipAuthorizationService relationshipService) {
        return new MaterialService(createMaterialInInstitutionUseCase, materialQueryUseCase,
                deleteMaterialUseCase, materialRepository, assetUrlService, relationshipService);
    }

    @Bean
    LessonObjectiveService lessonObjectiveService(
            CreateLessonObjectiveUseCase createLessonObjectiveUseCase,
            UpdateLessonObjectiveUseCase updateLessonObjectiveUseCase,
            DeleteLessonObjectiveUseCase deleteLessonObjectiveUseCase,
            LessonObjectiveQueryUseCase lessonObjectiveQueryUseCase,
            AddTopicsToLessonObjectiveUseCase addTopicsToLessonObjectiveUseCase,
            LessonObjectiveRepository lessonObjectiveRepository) {
        return new LessonObjectiveService(createLessonObjectiveUseCase, updateLessonObjectiveUseCase,
                deleteLessonObjectiveUseCase, lessonObjectiveQueryUseCase, addTopicsToLessonObjectiveUseCase,
                lessonObjectiveRepository);
    }
}
