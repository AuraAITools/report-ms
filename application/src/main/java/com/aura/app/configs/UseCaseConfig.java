package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorLessonLinkRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.InstitutionCreationRequestRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.core.repositories.LessonObjectiveTopicLinkRepository;
import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonQuizRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelLessonLinkRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.repositories.SchoolRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.repositories.StudentLessonReportRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectLessonLinkRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.repositories.email.EmailGateway;
import com.aura.core.repositories.email.EmailLogRepository;
import com.aura.core.repositories.email.TemplateRenderer;
import com.aura.core.usecases.accounts.AccountQueryUseCase;
import com.aura.core.usecases.accounts.AccountQueryUseCaseImpl;
import com.aura.core.usecases.accounts.AssignUserRoleUseCase;
import com.aura.core.usecases.accounts.AssignUserRoleUseCaseImpl;
import com.aura.core.usecases.accounts.CreateAccountAndTagToTenantUseCaseImpl;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCaseImpl;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCaseImpl;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCaseImpl;
import com.aura.core.usecases.authorization.GrantUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.GrantUserGroupRoleUseCaseImpl;
import com.aura.core.usecases.authorization.LookupGroupPermissionsUseCase;
import com.aura.core.usecases.authorization.LookupGroupPermissionsUseCaseImpl;
import com.aura.core.usecases.authorization.LookupPermissionsUseCase;
import com.aura.core.usecases.authorization.LookupPermissionsUseCaseImpl;
import com.aura.core.usecases.authorization.LookupUserGroupMembersUseCase;
import com.aura.core.usecases.authorization.LookupUserGroupMembersUseCaseImpl;
import com.aura.core.usecases.authorization.LookupUserGroupRelationsUseCase;
import com.aura.core.usecases.authorization.LookupUserGroupRelationsUseCaseImpl;
import com.aura.core.usecases.authorization.RevokeUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.RevokeUserGroupRoleUseCaseImpl;
import com.aura.core.usecases.courses.CourseQueryUseCase;
import com.aura.core.usecases.courses.CourseQueryUseCaseImpl;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import com.aura.core.usecases.courses.CreateCourseUseCaseImpl;
import com.aura.core.usecases.courses.DeleteCourseUseCase;
import com.aura.core.usecases.courses.DeleteCourseUseCaseImpl;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCaseImpl;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCaseImpl;
import com.aura.core.usecases.educators.DeleteEducatorUseCase;
import com.aura.core.usecases.educators.DeleteEducatorUseCaseImpl;
import com.aura.core.usecases.educators.EducatorQueryUseCase;
import com.aura.core.usecases.educators.EducatorQueryUseCaseImpl;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCaseImpl;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCaseImpl;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCaseImpl;
import com.aura.core.usecases.educators.UpdateEducatorUseCase;
import com.aura.core.usecases.educators.UpdateEducatorUseCaseImpl;
import com.aura.core.usecases.email.SendEmailUseCase;
import com.aura.core.usecases.email.SendEmailUseCaseImpl;
import com.aura.core.usecases.lesson_objectives.AddTopicsToLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.AddTopicsToLessonObjectiveUseCaseImpl;
import com.aura.core.usecases.lesson_objectives.CreateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.CreateLessonObjectiveUseCaseImpl;
import com.aura.core.usecases.lesson_objectives.DeleteLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.DeleteLessonObjectiveUseCaseImpl;
import com.aura.core.usecases.lesson_objectives.LessonObjectiveQueryUseCase;
import com.aura.core.usecases.lesson_objectives.LessonObjectiveQueryUseCaseImpl;
import com.aura.core.usecases.lesson_objectives.UpdateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.UpdateLessonObjectiveUseCaseImpl;
import com.aura.core.usecases.lesson_group_plans.AddMaterialsToLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.AddMaterialsToLessonGroupPlanUseCaseImpl;
import com.aura.core.usecases.lesson_group_plans.AddTopicsToLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.AddTopicsToLessonGroupPlanUseCaseImpl;
import com.aura.core.usecases.lesson_group_plans.CreateLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.CreateLessonGroupPlanUseCaseImpl;
import com.aura.core.usecases.lesson_group_plans.RemoveMaterialsFromLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.RemoveMaterialsFromLessonGroupPlanUseCaseImpl;
import com.aura.core.usecases.lesson_group_plans.RemoveTopicsFromLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.RemoveTopicsFromLessonGroupPlanUseCaseImpl;
import com.aura.core.usecases.lesson_group_plans.UpdateLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.UpdateLessonGroupPlanUseCaseImpl;
import com.aura.core.usecases.lesson_groups.AddStudentsToLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.AddStudentsToLessonGroupUseCaseImpl;
import com.aura.core.usecases.lesson_groups.CreateLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.CreateLessonGroupUseCaseImpl;
import com.aura.core.usecases.lesson_quizzes.CreateLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.CreateLessonQuizUseCaseImpl;
import com.aura.core.usecases.lesson_quizzes.DeleteLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.DeleteLessonQuizUseCaseImpl;
import com.aura.core.usecases.lesson_quizzes.LessonQuizQueryUseCase;
import com.aura.core.usecases.lesson_quizzes.LessonQuizQueryUseCaseImpl;
import com.aura.core.usecases.lesson_quizzes.UpdateLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.UpdateLessonQuizUseCaseImpl;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCaseImpl;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCaseImpl;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCaseImpl;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCaseImpl;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCaseImpl;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCaseImpl;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCaseImpl;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCaseImpl;
import com.aura.core.usecases.lessons.LinkLessonsToCourseUseCase;
import com.aura.core.usecases.lessons.LinkLessonsToCourseUseCaseImpl;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.CreateLevelUseCaseImpl;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCaseImpl;
import com.aura.core.usecases.materials.CreateMaterialInInstitutionUseCase;
import com.aura.core.usecases.materials.CreateMaterialInInstitutionUseCaseImpl;
import com.aura.core.usecases.materials.DeleteMaterialUseCase;
import com.aura.core.usecases.materials.DeleteMaterialUseCaseImpl;
import com.aura.core.usecases.materials.MaterialQueryUseCase;
import com.aura.core.usecases.materials.MaterialQueryUseCaseImpl;
import com.aura.core.usecases.institutions.OnboardInstitutionFromRequestUseCase;
import com.aura.core.usecases.institutions.OnboardInstitutionFromRequestUseCaseImpl;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCase;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCaseImpl;
import com.aura.core.usecases.onboarding.DefaultOnboardInstitutionUseCaseImpl;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import com.aura.core.usecases.onboarding.TenantInitialDataLoader;
import com.aura.core.usecases.outlet_rooms.CreateOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.CreateOutletRoomUseCaseImpl;
import com.aura.core.usecases.outlet_rooms.DeleteOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.DeleteOutletRoomUseCaseImpl;
import com.aura.core.usecases.outlet_rooms.OutletRoomQueryUseCase;
import com.aura.core.usecases.outlet_rooms.OutletRoomQueryUseCaseImpl;
import com.aura.core.usecases.outlet_rooms.UpdateOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.UpdateOutletRoomUseCaseImpl;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.CreateOutletUseCaseImpl;
import com.aura.core.usecases.outlets.DeleteOutletUseCase;
import com.aura.core.usecases.outlets.DeleteOutletUseCaseImpl;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCaseImpl;
import com.aura.core.usecases.outlets.UpdateOutletUseCase;
import com.aura.core.usecases.outlets.UpdateOutletUseCaseImpl;
import com.aura.core.usecases.parents.DeleteParentUseCase;
import com.aura.core.usecases.parents.DeleteParentUseCaseImpl;
import com.aura.core.usecases.parents.ParentQueryUseCase;
import com.aura.core.usecases.parents.ParentQueryUseCaseImpl;
import com.aura.core.usecases.parents.UpdateParentUseCase;
import com.aura.core.usecases.parents.UpdateParentUseCaseImpl;
import com.aura.core.usecases.schools.CreateSchoolUseCase;
import com.aura.core.usecases.schools.CreateSchoolUseCaseImpl;
import com.aura.core.usecases.schools.DeleteSchoolUseCase;
import com.aura.core.usecases.schools.DeleteSchoolUseCaseImpl;
import com.aura.core.usecases.schools.SchoolQueryUseCase;
import com.aura.core.usecases.schools.SchoolQueryUseCaseImpl;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCaseImpl;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.DeleteStudentUseCaseImpl;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.StudentQueryUseCaseImpl;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCaseImpl;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCaseImpl;
import com.aura.core.usecases.students.UpdateStudentUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCaseImpl;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateSubjectUseCaseImpl;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCaseImpl;
import com.aura.core.usecases.subjects.DeleteSubjectUseCase;
import com.aura.core.usecases.subjects.DeleteSubjectUseCaseImpl;
import com.aura.core.usecases.subjects.SubjectQueryUseCase;
import com.aura.core.usecases.subjects.SubjectQueryUseCaseImpl;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.CreateTopicUseCaseImpl;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCaseImpl;
import com.aura.core.usecases.topics.TopicQueryUseCase;
import com.aura.core.usecases.topics.TopicQueryUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    OnboardInstitutionUseCase onboardInstitutionUseCase(InstitutionRepository institutionRepository,
                                                        TenantInitialDataLoader tenantInitialDataLoader) {
        return new DefaultOnboardInstitutionUseCaseImpl(institutionRepository, tenantInitialDataLoader);
    }

    @Bean
    RequestInstitutionCreationUseCase requestInstitutionCreationUseCase(
            InstitutionRepository institutionRepository,
            InstitutionCreationRequestRepository institutionCreationRequestRepository) {
        return new RequestInstitutionCreationUseCaseImpl(institutionRepository, institutionCreationRequestRepository);
    }

    @Bean
    OnboardInstitutionFromRequestUseCase onboardInstitutionFromRequestUseCase(
            InstitutionCreationRequestRepository institutionCreationRequestRepository) {
        return new OnboardInstitutionFromRequestUseCaseImpl(institutionCreationRequestRepository);
    }

    @Bean
    AssignUserRoleUseCase assignUserRoleUseCase(IdentityService identityService,
                                                RelationshipAuthorizationService relationshipService) {
        return new AssignUserRoleUseCaseImpl(identityService, relationshipService);
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
    UpdateOutletUseCase updateOutletUseCase(OutletRepository outletRepository) {
        return new UpdateOutletUseCaseImpl(outletRepository);
    }

    @Bean
    CreateOutletRoomUseCase createOutletRoomUseCase(OutletRoomRepository outletRoomRepository) {
        return new CreateOutletRoomUseCaseImpl(outletRoomRepository);
    }

    @Bean
    UpdateOutletRoomUseCase updateOutletRoomUseCase(OutletRoomRepository outletRoomRepository) {
        return new UpdateOutletRoomUseCaseImpl(outletRoomRepository);
    }

    @Bean
    DeleteOutletRoomUseCase deleteOutletRoomUseCase(OutletRoomRepository outletRoomRepository) {
        return new DeleteOutletRoomUseCaseImpl(outletRoomRepository);
    }

    @Bean
    OutletRoomQueryUseCase outletRoomQueryUseCase(OutletRoomRepository outletRoomRepository) {
        return new OutletRoomQueryUseCaseImpl(outletRoomRepository);
    }

    @Bean
    CreateCourseUseCase createCourseUseCase(CourseRepository courseRepository, OutletRepository outletRepository) {
        return new CreateCourseUseCaseImpl(courseRepository, outletRepository);
    }

    @Bean
    CourseQueryUseCase courseQueryUseCase(CourseRepository courseRepository) {
        return new CourseQueryUseCaseImpl(courseRepository);
    }

    @Bean
    CreateLessonUseCase createLessonUseCase(LessonRepository lessonRepository,
                                            CourseRepository courseRepository) {
        return new CreateLessonUseCaseImpl(lessonRepository, courseRepository);
    }

    @Bean
    LessonQueryUseCase lessonQueryUseCase(LessonRepository lessonRepository) {
        return new LessonQueryUseCaseImpl(lessonRepository);
    }

    @Bean
    SubjectQueryUseCase subjectQueryUseCase(SubjectRepository subjectRepository) {
        return new SubjectQueryUseCaseImpl(subjectRepository);
    }

    @Bean
    SchoolQueryUseCase schoolQueryUseCase(SchoolRepository schoolRepository) {
        return new SchoolQueryUseCaseImpl(schoolRepository);
    }

    @Bean
    CreateSchoolUseCase createSchoolUseCase(SchoolRepository schoolRepository) {
        return new CreateSchoolUseCaseImpl(schoolRepository);
    }

    @Bean
    DeleteSchoolUseCase deleteSchoolUseCase(SchoolRepository schoolRepository) {
        return new DeleteSchoolUseCaseImpl(schoolRepository);
    }

    @Bean
    TopicQueryUseCase topicQueryUseCase(TopicRepository topicRepository) {
        return new TopicQueryUseCaseImpl(topicRepository);
    }

    @Bean
    LinkLessonsToCourseUseCase linkLessonsToCourseUseCase(LessonRepository lessonRepository, CourseRepository courseRepository) {
        return new LinkLessonsToCourseUseCaseImpl(lessonRepository, courseRepository);
    }

    @Bean
    AddEducatorsToLessonUseCase addEducatorsToLessonUseCase(EducatorLessonLinkRepository educatorLessonLinkRepository,
                                                            LessonRepository lessonRepository,
                                                            EducatorRepository educatorRepository) {
        return new AddEducatorsToLessonUseCaseImpl(educatorLessonLinkRepository, lessonRepository, educatorRepository);
    }

    @Bean
    AddStudentsToLessonUseCase addStudentsToLessonUseCase(StudentLessonLinkRepository studentLessonLinkRepository,
                                                          LessonRepository lessonRepository,
                                                          StudentRepository studentRepository) {
        return new AddStudentsToLessonUseCaseImpl(studentLessonLinkRepository, lessonRepository, studentRepository);
    }

    @Bean
    AddSubjectsToLessonUseCase addSubjectsToLessonUseCase(SubjectLessonLinkRepository subjectLessonLinkRepository,
                                                          LessonRepository lessonRepository,
                                                          SubjectRepository subjectRepository) {
        return new AddSubjectsToLessonUseCaseImpl(subjectLessonLinkRepository, lessonRepository, subjectRepository);
    }

    @Bean
    AddLevelsToLessonUseCase addLevelsToLessonUseCase(LevelLessonLinkRepository levelLessonLinkRepository,
                                                      LessonRepository lessonRepository,
                                                      LevelRepository levelRepository) {
        return new AddLevelsToLessonUseCaseImpl(levelLessonLinkRepository, lessonRepository, levelRepository);
    }

    @Bean
    AddTopicsToLessonUseCase addTopicsToLessonUseCase(TopicLessonLinkRepository topicLessonLinkRepository,
                                                      LessonRepository lessonRepository,
                                                      TopicRepository topicRepository) {
        return new AddTopicsToLessonUseCaseImpl(topicLessonLinkRepository, lessonRepository, topicRepository);
    }

    @Bean
    CreateLessonGroupPlanUseCase createLessonGroupPlanUseCase(LessonGroupPlanRepository lessonGroupPlanRepository,
                                                              LessonGroupRepository lessonGroupRepository) {
        return new CreateLessonGroupPlanUseCaseImpl(lessonGroupPlanRepository, lessonGroupRepository);
    }

    @Bean
    CreateLessonGroupUseCase createLessonGroupUseCase(LessonGroupRepository lessonGroupRepository,
                                                      LessonRepository lessonRepository) {
        return new CreateLessonGroupUseCaseImpl(lessonGroupRepository, lessonRepository);
    }

    @Bean
    CreateStudentLessonReportUseCase createStudentLessonReportUseCase(StudentLessonReportRepository studentLessonReportRepository) {
        return new CreateStudentLessonReportUseCaseImpl(studentLessonReportRepository);
    }

    @Bean
    AddTopicsToLessonGroupPlanUseCase addTopicsToLessonGroupPlanUseCase(LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository,
                                                                        LessonGroupPlanRepository lessonGroupPlanRepository,
                                                                        TopicRepository topicRepository) {
        return new AddTopicsToLessonGroupPlanUseCaseImpl(lessonGroupPlanTopicLinkRepository, lessonGroupPlanRepository, topicRepository);
    }

    @Bean
    AddMaterialsToLessonGroupPlanUseCase addMaterialsToLessonGroupPlanUseCase(LessonGroupPlanMaterialLinkRepository lessonGroupPlanMaterialLinkRepository,
                                                                               LessonGroupPlanRepository lessonGroupPlanRepository,
                                                                               MaterialRepository materialRepository) {
        return new AddMaterialsToLessonGroupPlanUseCaseImpl(lessonGroupPlanMaterialLinkRepository, lessonGroupPlanRepository, materialRepository);
    }

    @Bean
    UpdateLessonGroupPlanUseCase updateLessonGroupPlanUseCase(LessonGroupPlanRepository lessonGroupPlanRepository) {
        return new UpdateLessonGroupPlanUseCaseImpl(lessonGroupPlanRepository);
    }

    @Bean
    RemoveMaterialsFromLessonGroupPlanUseCase removeMaterialsFromLessonGroupPlanUseCase(
            LessonGroupPlanMaterialLinkRepository lessonGroupPlanMaterialLinkRepository) {
        return new RemoveMaterialsFromLessonGroupPlanUseCaseImpl(lessonGroupPlanMaterialLinkRepository);
    }

    @Bean
    RemoveTopicsFromLessonGroupPlanUseCase removeTopicsFromLessonGroupPlanUseCase(
            LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository) {
        return new RemoveTopicsFromLessonGroupPlanUseCaseImpl(lessonGroupPlanTopicLinkRepository);
    }

    @Bean
    AddStudentsToLessonGroupUseCase addStudentsToLessonGroupUseCase(LessonGroupRepository lessonGroupRepository,
                                                                    StudentLessonLinkRepository studentLessonLinkRepository) {
        return new AddStudentsToLessonGroupUseCaseImpl(lessonGroupRepository, studentLessonLinkRepository);
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
    EnrollStudentToCourseUseCase enrollStudentToCourseUseCase(StudentRepository studentRepository) {
        return new EnrollStudentToCourseUseCaseImpl(studentRepository);
    }

    @Bean
    StudentQueryUseCase studentQueryUseCase(StudentRepository studentRepository) {
        return new StudentQueryUseCaseImpl(studentRepository);
    }

    @Bean
    UpdateStudentUseCase updateStudentUseCase(StudentRepository studentRepository) {
        return new UpdateStudentUseCaseImpl(studentRepository);
    }

    @Bean
    DeleteStudentUseCase deleteStudentUseCase(StudentRepository studentRepository) {
        return new DeleteStudentUseCaseImpl(studentRepository);
    }

    @Bean
    CreateEducatorInAccountUseCase createEducatorInAccountUseCase(AccountRepository accountRepository, EducatorRepository educatorRepository) {
        return new CreateEducatorInAccountUseCaseImpl(accountRepository, educatorRepository);
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
    EducatorQueryUseCase educatorQueryUseCase(EducatorRepository educatorRepository) {
        return new EducatorQueryUseCaseImpl(educatorRepository);
    }

    @Bean
    UpdateEducatorUseCase updateEducatorUseCase(EducatorRepository educatorRepository) {
        return new UpdateEducatorUseCaseImpl(educatorRepository);
    }

    @Bean
    DeleteEducatorUseCase deleteEducatorUseCase(EducatorRepository educatorRepository) {
        return new DeleteEducatorUseCaseImpl(educatorRepository);
    }

    @Bean
    ParentQueryUseCase parentQueryUseCase(ParentRepository parentRepository) {
        return new ParentQueryUseCaseImpl(parentRepository);
    }

    @Bean
    UpdateParentUseCase updateParentUseCase(ParentRepository parentRepository) {
        return new UpdateParentUseCaseImpl(parentRepository);
    }

    @Bean
    DeleteParentUseCase deleteParentUseCase(ParentRepository parentRepository) {
        return new DeleteParentUseCaseImpl(parentRepository);
    }


    @Bean
    CreateTopicUseCase createTopicUseCase(TopicRepository topicRepository) {
        return new CreateTopicUseCaseImpl(topicRepository);
    }

    @Bean
    CreateSubjectUseCase createSubjectUseCase(SubjectRepository subjectRepository) {
        return new CreateSubjectUseCaseImpl(subjectRepository);
    }

    @Bean
    CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase(SubjectRepository subjectRepository) {
        return new CreateTopicSubjectLinksUseCaseImpl(subjectRepository);
    }

    @Bean
    LookupPermissionsUseCase lookupPermissionsUseCase(RelationshipAuthorizationService relationshipService) {
        return new LookupPermissionsUseCaseImpl(relationshipService);
    }

    @Bean
    LookupGroupPermissionsUseCase lookupGroupPermissionsUseCase(RelationshipAuthorizationService relationshipService) {
        return new LookupGroupPermissionsUseCaseImpl(relationshipService);
    }

    @Bean
    LookupUserGroupRelationsUseCase lookupUserGroupRelationsUseCase(RelationshipAuthorizationService relationshipService) {
        return new LookupUserGroupRelationsUseCaseImpl(relationshipService);
    }

    @Bean
    LookupUserGroupMembersUseCase lookupUserGroupMembersUseCase(RelationshipAuthorizationService relationshipService) {
        return new LookupUserGroupMembersUseCaseImpl(relationshipService);
    }

    @Bean
    GrantUserGroupRoleUseCase grantUserGroupRoleUseCase(RelationshipAuthorizationService relationshipService) {
        return new GrantUserGroupRoleUseCaseImpl(relationshipService);
    }

    @Bean
    RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase(RelationshipAuthorizationService relationshipService) {
        return new RevokeUserGroupRoleUseCaseImpl(relationshipService);
    }

    @Bean
    SendEmailUseCase sendEmailUseCase(EmailLogRepository emailLogRepository,
                                      EmailGateway emailGateway,
                                      TemplateRenderer templateRenderer) {
        return new SendEmailUseCaseImpl(emailLogRepository, emailGateway, templateRenderer);
    }

    @Bean
    DeleteCourseUseCase deleteCourseUseCase(CourseRepository courseRepository) {
        return new DeleteCourseUseCaseImpl(courseRepository);
    }

    @Bean
    DeleteLessonUseCase deleteLessonUseCase(LessonRepository lessonRepository) {
        return new DeleteLessonUseCaseImpl(lessonRepository);
    }

    @Bean
    DeleteLevelUseCase deleteLevelUseCase(LevelRepository levelRepository) {
        return new DeleteLevelUseCaseImpl(levelRepository);
    }

    @Bean
    DeleteOutletUseCase deleteOutletUseCase(OutletRepository outletRepository) {
        return new DeleteOutletUseCaseImpl(outletRepository);
    }

    @Bean
    DeleteSubjectUseCase deleteSubjectUseCase(SubjectRepository subjectRepository) {
        return new DeleteSubjectUseCaseImpl(subjectRepository);
    }

    @Bean
    DeleteTopicUseCase deleteTopicUseCase(TopicRepository topicRepository) {
        return new DeleteTopicUseCaseImpl(topicRepository);
    }

    @Bean
    AccountQueryUseCase accountQueryUseCase(AccountRepository accountRepository, ParentRepository parentRepository) {
        return new AccountQueryUseCaseImpl(accountRepository, parentRepository);
    }

    @Bean
    CreateLevelUseCase createLevelUseCase(LevelRepository levelRepository) {
        return new CreateLevelUseCaseImpl(levelRepository);
    }

    @Bean
    CreateMaterialInInstitutionUseCase createMaterialInInstitutionUseCase(MaterialRepository materialRepository) {
        return new CreateMaterialInInstitutionUseCaseImpl(materialRepository);
    }

    @Bean
    MaterialQueryUseCase materialQueryUseCase(MaterialRepository materialRepository) {
        return new MaterialQueryUseCaseImpl(materialRepository);
    }

    @Bean
    DeleteMaterialUseCase deleteMaterialUseCase(MaterialRepository materialRepository) {
        return new DeleteMaterialUseCaseImpl(materialRepository);
    }

    @Bean
    CreateLessonObjectiveUseCase createLessonObjectiveUseCase(LessonObjectiveRepository lessonObjectiveRepository) {
        return new CreateLessonObjectiveUseCaseImpl(lessonObjectiveRepository);
    }

    @Bean
    UpdateLessonObjectiveUseCase updateLessonObjectiveUseCase(LessonObjectiveRepository lessonObjectiveRepository) {
        return new UpdateLessonObjectiveUseCaseImpl(lessonObjectiveRepository);
    }

    @Bean
    DeleteLessonObjectiveUseCase deleteLessonObjectiveUseCase(LessonObjectiveRepository lessonObjectiveRepository) {
        return new DeleteLessonObjectiveUseCaseImpl(lessonObjectiveRepository);
    }

    @Bean
    LessonObjectiveQueryUseCase lessonObjectiveQueryUseCase(LessonObjectiveRepository lessonObjectiveRepository) {
        return new LessonObjectiveQueryUseCaseImpl(lessonObjectiveRepository);
    }

    @Bean
    AddTopicsToLessonObjectiveUseCase addTopicsToLessonObjectiveUseCase(
            LessonObjectiveTopicLinkRepository lessonObjectiveTopicLinkRepository,
            TopicRepository topicRepository) {
        return new AddTopicsToLessonObjectiveUseCaseImpl(lessonObjectiveTopicLinkRepository, topicRepository);
    }

    @Bean
    CreateLessonQuizUseCase createLessonQuizUseCase(LessonQuizRepository lessonQuizRepository,
                                                     LessonGroupPlanRepository lessonGroupPlanRepository) {
        return new CreateLessonQuizUseCaseImpl(lessonQuizRepository, lessonGroupPlanRepository);
    }

    @Bean
    UpdateLessonQuizUseCase updateLessonQuizUseCase(LessonQuizRepository lessonQuizRepository) {
        return new UpdateLessonQuizUseCaseImpl(lessonQuizRepository);
    }

    @Bean
    DeleteLessonQuizUseCase deleteLessonQuizUseCase(LessonQuizRepository lessonQuizRepository) {
        return new DeleteLessonQuizUseCaseImpl(lessonQuizRepository);
    }

    @Bean
    LessonQuizQueryUseCase lessonQuizQueryUseCase(LessonQuizRepository lessonQuizRepository) {
        return new LessonQuizQueryUseCaseImpl(lessonQuizRepository);
    }
}
