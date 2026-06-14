package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.LessonGroupRepository;
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
import com.aura.core.usecases.institutions.OnboardInstitutionFromRequestUseCase;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCase;
import com.aura.core.usecases.lesson_objectives.AddTopicsToLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.CreateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.DeleteLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.LessonObjectiveQueryUseCase;
import com.aura.core.usecases.lesson_objectives.UpdateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_quizzes.CreateLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.DeleteLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.LessonQuizQueryUseCase;
import com.aura.core.usecases.lesson_quizzes.UpdateLessonQuizUseCase;
import com.aura.core.usecases.lesson_group_plans.AddMaterialsToLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.AddTopicsToLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.CreateLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.RemoveMaterialsFromLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.RemoveTopicsFromLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.UpdateLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_groups.AddStudentsToLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.CreateLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.DeleteLessonGroupUseCase;
import com.aura.core.usecases.lessons.ChangeLessonOutletRoomUseCase;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.DetectLessonConflictsUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import com.aura.core.usecases.levels.GetLevelDeleteImpactUseCase;
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
import com.aura.core.usecases.schools.GetSchoolDeleteImpactUseCase;
import com.aura.core.usecases.schools.SchoolQueryUseCase;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
import com.aura.core.usecases.lesson_homework_completions.CreateLessonHomeworkCompletionUseCase;
import com.aura.core.usecases.lesson_homework_completions.DeleteLessonHomeworkCompletionUseCase;
import com.aura.core.usecases.lesson_homework_completions.LessonHomeworkCompletionQueryUseCase;
import com.aura.core.usecases.lesson_homework_completions.UpdateLessonHomeworkCompletionUseCase;
import com.aura.core.usecases.lesson_participation_reviews.CreateLessonParticipationReviewUseCase;
import com.aura.core.usecases.lesson_participation_reviews.DeleteLessonParticipationReviewUseCase;
import com.aura.core.usecases.lesson_participation_reviews.LessonParticipationReviewQueryUseCase;
import com.aura.core.usecases.lesson_participation_reviews.UpdateLessonParticipationReviewUseCase;
import com.aura.core.usecases.lesson_topic_proficiencies.CreateLessonTopicProficiencyUseCase;
import com.aura.core.usecases.lesson_topic_proficiencies.DeleteLessonTopicProficiencyUseCase;
import com.aura.core.usecases.lesson_topic_proficiencies.LessonTopicProficiencyQueryUseCase;
import com.aura.core.usecases.lesson_topic_proficiencies.UpdateLessonTopicProficiencyUseCase;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.DeleteStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.StudentLessonReportQueryUseCase;
import com.aura.core.usecases.student_lesson_reports.UpdateStudentLessonReportUseCase;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCase;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import com.aura.core.usecases.subjects.DeleteSubjectUseCase;
import com.aura.core.usecases.subjects.GetSubjectDeleteImpactUseCase;
import com.aura.core.usecases.subjects.SubjectQueryUseCase;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import com.aura.core.usecases.topics.GetTopicDeleteImpactUseCase;
import com.aura.core.usecases.topics.TopicQueryUseCase;
import com.aura.details.institutions.onboarding.TenantInitialDataLoaderImpl;
import com.aura.details.services.AccountService;
import com.aura.details.services.AssetUrlService;
import com.aura.details.services.AuthorizationService;
import com.aura.details.services.CourseService;
import com.aura.details.services.EducatorService;
import com.aura.details.services.LessonGroupPlanService;
import com.aura.details.services.LessonGroupService;
import com.aura.details.services.LessonObjectiveService;
import com.aura.details.services.LessonService;
import com.aura.details.services.LevelService;
import com.aura.details.services.MaterialService;
import com.aura.details.services.InstitutionCreationRequestService;
import com.aura.details.services.OnboardingService;
import com.aura.details.services.OutletRoomService;
import com.aura.details.services.OutletService;
import com.aura.details.services.ParentService;
import com.aura.details.services.SchoolService;
import com.aura.details.services.LessonHomeworkCompletionService;
import com.aura.details.services.LessonParticipationReviewService;
import com.aura.details.services.LessonTopicProficiencyService;
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
    InstitutionCreationRequestService institutionCreationRequestService(
            RequestInstitutionCreationUseCase requestInstitutionCreationUseCase,
            OnboardInstitutionFromRequestUseCase onboardInstitutionFromRequestUseCase,
            OnboardingService onboardingService,
            AccountService accountService,
            RelationshipAuthorizationService relationshipService) {
        return new InstitutionCreationRequestService(
                requestInstitutionCreationUseCase,
                onboardInstitutionFromRequestUseCase,
                onboardingService,
                accountService,
                relationshipService);
    }

    @Bean
    InstitutionQueryUseCase institutionQueryService(InstitutionRepository institutionRepository) {
        return new InstitutionQueryUseCaseImpl(institutionRepository);
    }

    @Bean
    com.aura.details.services.InstitutionService institutionService(
            InstitutionRepository institutionRepository,
            InstitutionQueryUseCase institutionQueryUseCase,
            AssetUrlService assetUrlService,
            com.aura.details.services.ProfileImageValidator profileImageValidator) {
        return new com.aura.details.services.InstitutionService(
                institutionRepository, institutionQueryUseCase, assetUrlService, profileImageValidator);
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
                                  AccountQueryUseCase accountQueryUseCase,
                                  AssetUrlService assetUrlService,
                                  com.aura.details.services.ProfileImageValidator profileImageValidator) {
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
                accountQueryUseCase,
                assetUrlService,
                profileImageValidator);
    }

    @Bean
    com.aura.details.services.ProfileImageValidator profileImageValidator() {
        return new com.aura.details.services.ProfileImageValidator();
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
                                        OutletRoomRepository outletRoomRepository) {
        return new OutletRoomService(createOutletRoomUseCase, updateOutletRoomUseCase, deleteOutletRoomUseCase,
                outletRoomQueryUseCase, outletRoomRepository);
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
                                DetectLessonConflictsUseCase detectLessonConflictsUseCase,
                                AddEducatorsToLessonUseCase addEducatorsToLessonUseCase,
                                AddStudentsToLessonUseCase addStudentsToLessonUseCase,
                                AddSubjectsToLessonUseCase addSubjectsToLessonUseCase,
                                AddLevelsToLessonUseCase addLevelsToLessonUseCase,
                                AddTopicsToLessonUseCase addTopicsToLessonUseCase,
                                LessonQueryUseCase lessonQueryUseCase,
                                DeleteLessonUseCase deleteLessonUseCase,
                                ChangeLessonOutletRoomUseCase changeLessonOutletRoomUseCase,
                                LessonRepository lessonRepository,
                                LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository,
                                RelationshipAuthorizationService relationshipService,
                                EducatorRepository educatorRepository,
                                StudentRepository studentRepository,
                                LessonGroupService lessonGroupService,
                                LessonGroupPlanService lessonGroupPlanService,
                                StudentLessonReportService studentLessonReportService) {
        return new LessonService(createLessonUseCase, detectLessonConflictsUseCase,
                addEducatorsToLessonUseCase, addStudentsToLessonUseCase, addSubjectsToLessonUseCase,
                addLevelsToLessonUseCase, addTopicsToLessonUseCase, lessonQueryUseCase,
                deleteLessonUseCase, changeLessonOutletRoomUseCase, lessonRepository,
                lessonOutletRoomBookingRepository, relationshipService, educatorRepository,
                studentRepository, lessonGroupService, lessonGroupPlanService,
                studentLessonReportService);
    }

    @Bean
    LessonGroupService lessonGroupService(CreateLessonGroupUseCase createLessonGroupUseCase,
                                          AddStudentsToLessonGroupUseCase addStudentsToLessonGroupUseCase,
                                          DeleteLessonGroupUseCase deleteLessonGroupUseCase,
                                          LessonGroupRepository lessonGroupRepository) {
        return new LessonGroupService(createLessonGroupUseCase, addStudentsToLessonGroupUseCase, deleteLessonGroupUseCase, lessonGroupRepository);
    }

    @Bean
    LessonGroupPlanService lessonGroupPlanService(CreateLessonGroupPlanUseCase createLessonGroupPlanUseCase,
                                                   UpdateLessonGroupPlanUseCase updateLessonGroupPlanUseCase,
                                                   AddTopicsToLessonGroupPlanUseCase addTopicsToLessonGroupPlanUseCase,
                                                   AddMaterialsToLessonGroupPlanUseCase addMaterialsToLessonGroupPlanUseCase,
                                                   RemoveTopicsFromLessonGroupPlanUseCase removeTopicsFromLessonGroupPlanUseCase,
                                                   RemoveMaterialsFromLessonGroupPlanUseCase removeMaterialsFromLessonGroupPlanUseCase,
                                                   LessonGroupPlanRepository lessonGroupPlanRepository,
                                                   LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository,
                                                   TopicLessonLinkRepository topicLessonLinkRepository,
                                                   LessonGroupRepository lessonGroupRepository,
                                                   LessonObjectiveService lessonObjectiveService) {
        return new LessonGroupPlanService(createLessonGroupPlanUseCase, updateLessonGroupPlanUseCase,
                addTopicsToLessonGroupPlanUseCase, addMaterialsToLessonGroupPlanUseCase,
                removeTopicsFromLessonGroupPlanUseCase, removeMaterialsFromLessonGroupPlanUseCase,
                lessonGroupPlanRepository, lessonGroupPlanTopicLinkRepository,
                topicLessonLinkRepository, lessonGroupRepository, lessonObjectiveService);
    }

    @Bean
    StudentLessonReportService studentLessonReportService(CreateStudentLessonReportUseCase createStudentLessonReportUseCase,
                                                          StudentLessonReportQueryUseCase studentLessonReportQueryUseCase,
                                                          UpdateStudentLessonReportUseCase updateStudentLessonReportUseCase,
                                                          DeleteStudentLessonReportUseCase deleteStudentLessonReportUseCase,
                                                          StudentLessonLinkRepository studentLessonLinkRepository,
                                                          LessonGroupPlanRepository lessonGroupPlanRepository,
                                                          LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository) {
        return new StudentLessonReportService(createStudentLessonReportUseCase,
                studentLessonReportQueryUseCase, updateStudentLessonReportUseCase,
                deleteStudentLessonReportUseCase, studentLessonLinkRepository,
                lessonGroupPlanRepository, lessonGroupPlanTopicLinkRepository);
    }

    @Bean
    LessonHomeworkCompletionService lessonHomeworkCompletionService(
            CreateLessonHomeworkCompletionUseCase createUseCase,
            LessonHomeworkCompletionQueryUseCase queryUseCase,
            UpdateLessonHomeworkCompletionUseCase updateUseCase,
            DeleteLessonHomeworkCompletionUseCase deleteUseCase) {
        return new LessonHomeworkCompletionService(createUseCase, queryUseCase, updateUseCase, deleteUseCase);
    }

    @Bean
    LessonParticipationReviewService lessonParticipationReviewService(
            CreateLessonParticipationReviewUseCase createUseCase,
            LessonParticipationReviewQueryUseCase queryUseCase,
            UpdateLessonParticipationReviewUseCase updateUseCase,
            DeleteLessonParticipationReviewUseCase deleteUseCase) {
        return new LessonParticipationReviewService(createUseCase, queryUseCase, updateUseCase, deleteUseCase);
    }

    @Bean
    LessonTopicProficiencyService lessonTopicProficiencyService(
            CreateLessonTopicProficiencyUseCase createUseCase,
            LessonTopicProficiencyQueryUseCase queryUseCase,
            UpdateLessonTopicProficiencyUseCase updateUseCase,
            DeleteLessonTopicProficiencyUseCase deleteUseCase) {
        return new LessonTopicProficiencyService(createUseCase, queryUseCase, updateUseCase, deleteUseCase);
    }

    @Bean
    LevelService levelService(CreateLevelUseCase createLevelUseCase,
                              DeleteLevelUseCase deleteLevelUseCase,
                              GetLevelDeleteImpactUseCase getLevelDeleteImpactUseCase,
                              LevelRepository levelRepository,
                              CourseRepository courseRepository) {
        return new LevelService(createLevelUseCase, deleteLevelUseCase, getLevelDeleteImpactUseCase,
                levelRepository, courseRepository);
    }

    @Bean
    TopicService topicService(
            CreateTopicUseCase createTopicUseCase,
            TopicQueryUseCase topicQueryUseCase,
            DeleteTopicUseCase deleteTopicUseCase,
            GetTopicDeleteImpactUseCase getTopicDeleteImpactUseCase,
            TopicRepository topicRepository) {
        return new TopicService(createTopicUseCase, topicQueryUseCase, deleteTopicUseCase,
                getTopicDeleteImpactUseCase, topicRepository);
    }

    @Bean
    SubjectService subjectService(
            CreateSubjectUseCase createSubjectUseCase,
            CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase,
            SubjectQueryUseCase subjectQueryUseCase,
            DeleteSubjectUseCase deleteSubjectUseCase,
            GetSubjectDeleteImpactUseCase getSubjectDeleteImpactUseCase,
            SubjectRepository subjectRepository) {
        return new SubjectService(createSubjectUseCase, createTopicSubjectLinksUseCase, subjectQueryUseCase,
                deleteSubjectUseCase, getSubjectDeleteImpactUseCase, subjectRepository);
    }

    @Bean
    SchoolService schoolService(SchoolQueryUseCase schoolQueryUseCase,
                                CreateSchoolUseCase createSchoolUseCase,
                                DeleteSchoolUseCase deleteSchoolUseCase,
                                GetSchoolDeleteImpactUseCase getSchoolDeleteImpactUseCase,
                                SchoolRepository schoolRepository) {
        return new SchoolService(schoolQueryUseCase, createSchoolUseCase, deleteSchoolUseCase,
                getSchoolDeleteImpactUseCase, schoolRepository);
    }

    @Bean
    StudentService studentService(StudentQueryUseCase studentQueryUseCase,
                                  UpdateStudentUseCase updateStudentUseCase,
                                  DeleteStudentUseCase deleteStudentUseCase,
                                  StudentRepository studentRepository,
                                  AssetUrlService assetUrlService,
                                  com.aura.details.services.ProfileImageValidator profileImageValidator) {
        return new StudentService(studentQueryUseCase, updateStudentUseCase, deleteStudentUseCase,
                studentRepository, assetUrlService, profileImageValidator);
    }

    @Bean
    EducatorService educatorService(EducatorQueryUseCase educatorQueryUseCase,
                                    UpdateEducatorUseCase updateEducatorUseCase,
                                    DeleteEducatorUseCase deleteEducatorUseCase,
                                    EducatorRepository educatorRepository,
                                    AssetUrlService assetUrlService,
                                    com.aura.details.services.ProfileImageValidator profileImageValidator) {
        return new EducatorService(educatorQueryUseCase, updateEducatorUseCase, deleteEducatorUseCase,
                educatorRepository, assetUrlService, profileImageValidator);
    }

    @Bean
    ParentService parentService(ParentQueryUseCase parentQueryUseCase,
                                UpdateParentUseCase updateParentUseCase,
                                DeleteParentUseCase deleteParentUseCase,
                                ParentRepository parentRepository,
                                AssetUrlService assetUrlService,
                                com.aura.details.services.ProfileImageValidator profileImageValidator) {
        return new ParentService(parentQueryUseCase, updateParentUseCase, deleteParentUseCase,
                parentRepository, assetUrlService, profileImageValidator);
    }

    @Bean
    AuthorizationService authorizationService(LookupPermissionsUseCase lookupPermissionsUseCase,
                                              com.aura.core.usecases.authorization.LookupGroupPermissionsUseCase lookupGroupPermissionsUseCase,
                                              com.aura.core.usecases.authorization.LookupUserGroupRelationsUseCase lookupUserGroupRelationsUseCase,
                                              com.aura.core.usecases.authorization.LookupUserGroupMembersUseCase lookupUserGroupMembersUseCase,
                                              GrantUserGroupRoleUseCase grantUserGroupRoleUseCase,
                                              RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase,
                                              com.aura.details.services.UserGroupResourceResolver userGroupResourceResolver,
                                              com.aura.core.repositories.InstitutionRepository institutionRepository,
                                              OutletRepository outletRepository,
                                              com.aura.core.repositories.CourseRepository courseRepository,
                                              com.aura.core.repositories.LessonRepository lessonRepository,
                                              com.aura.core.repositories.MaterialRepository materialRepository,
                                              AccountRepository accountRepository,
                                              org.springframework.context.ApplicationEventPublisher applicationEventPublisher) {
        return new AuthorizationService(lookupPermissionsUseCase, lookupGroupPermissionsUseCase,
                lookupUserGroupRelationsUseCase, lookupUserGroupMembersUseCase,
                grantUserGroupRoleUseCase, revokeUserGroupRoleUseCase,
                userGroupResourceResolver, institutionRepository, outletRepository,
                courseRepository, lessonRepository, materialRepository, accountRepository,
                applicationEventPublisher);
    }

    @Bean
    com.aura.details.services.UserGroupResourceResolver userGroupResourceResolver(
            com.aura.core.repositories.InstitutionRepository institutionRepository,
            OutletRepository outletRepository,
            com.aura.core.repositories.CourseRepository courseRepository,
            com.aura.core.repositories.LessonRepository lessonRepository,
            AccountRepository accountRepository,
            com.aura.core.repositories.MaterialRepository materialRepository) {
        return new com.aura.details.services.UserGroupResourceResolver(
                institutionRepository, outletRepository, courseRepository, lessonRepository,
                accountRepository, materialRepository);
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

    @Bean
    com.aura.details.services.LessonQuizService lessonQuizService(
            CreateLessonQuizUseCase createLessonQuizUseCase,
            UpdateLessonQuizUseCase updateLessonQuizUseCase,
            DeleteLessonQuizUseCase deleteLessonQuizUseCase,
            LessonQuizQueryUseCase lessonQuizQueryUseCase) {
        return new com.aura.details.services.LessonQuizService(createLessonQuizUseCase, updateLessonQuizUseCase,
                deleteLessonQuizUseCase, lessonQuizQueryUseCase);
    }
}
