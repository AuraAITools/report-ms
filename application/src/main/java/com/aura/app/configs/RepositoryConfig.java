package com.aura.app.configs;

import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorLessonLinkRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.core.repositories.LessonObjectiveTopicLinkRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelLessonLinkRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.repositories.PriceRecordRepository;
import com.aura.core.repositories.SchoolRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.repositories.StudentLessonReportRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectLessonLinkRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.repositories.email.EmailLogRepository;
import com.aura.details.springdata.repositories.impl.AccountRepositoryImpl;
import com.aura.details.springdata.repositories.impl.CourseRepositoryImpl;
import com.aura.details.springdata.repositories.impl.EducatorLessonLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.EducatorRepositoryImpl;
import com.aura.details.springdata.repositories.impl.EmailLogRepositoryImpl;
import com.aura.details.springdata.repositories.impl.InstitutionRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonGroupPlanMaterialLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonGroupPlanRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonGroupPlanTopicLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonGroupRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonObjectiveRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonObjectiveTopicLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonOutletRoomBookingRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LevelLessonLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LevelRepositoryImpl;
import com.aura.details.springdata.repositories.impl.MaterialRepositoryImpl;
import com.aura.details.springdata.repositories.impl.OutletRepositoryImpl;
import com.aura.details.springdata.repositories.impl.OutletRoomRepositoryImpl;
import com.aura.details.springdata.repositories.impl.ParentRepositoryImpl;
import com.aura.details.springdata.repositories.impl.PriceRecordRepositoryImpl;
import com.aura.details.springdata.repositories.impl.SchoolRepositoryImpl;
import com.aura.details.springdata.repositories.impl.StudentLessonLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.StudentLessonReportRepositoryImpl;
import com.aura.details.springdata.repositories.impl.StudentRepositoryImpl;
import com.aura.details.springdata.repositories.impl.SubjectLessonLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.SubjectRepositoryImpl;
import com.aura.details.springdata.repositories.impl.TopicLessonLinkRepositoryImpl;
import com.aura.details.springdata.repositories.impl.TopicRepositoryImpl;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.repositories.jpa.accounts.JpaParentRepository;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.email.JpaEmailLogRepository;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonGroupPlanRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonGroupRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonObjectiveRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonOutletRoomBookingRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaPriceRecordRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanMaterialsLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonObjectiveTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaMaterialTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonReportRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentOutletLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.materials.JpaMaterialRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRoomRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    InstitutionRepository institutionRepository(JpaInstitutionRepository jpaRepository) {
        return new InstitutionRepositoryImpl(jpaRepository);
    }

    @Bean
    LevelRepository levelRepository(JpaLevelRepository jpaRepository,
                                    JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository,
                                    JpaCourseRepository jpaCourseRepository,
                                    JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository) {
        return new LevelRepositoryImpl(jpaRepository, jpaLevelLessonLinkRepository, jpaCourseRepository, jpaLevelEducatorLinkRepository);
    }

    @Bean
    AccountRepository accountRepository(JpaAccountRepository jpaAccountRepository, JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository, JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository, JpaStudentRepository jpaStudentRepository, JpaEducatorRepository jpaEducatorRepository) {
        return new AccountRepositoryImpl(jpaAccountRepository, jpaAccountStudentLinkRepository, jpaAccountEducatorLinkRepository, jpaStudentRepository, jpaEducatorRepository);
    }

    @Bean
    ParentRepository parentRepository(JpaParentRepository jpaParentRepository, EntityManager entityManager) {
        return new ParentRepositoryImpl(jpaParentRepository, entityManager);
    }

    @Bean
    StudentRepository studentRepository(JpaStudentRepository jpaStudentRepository,
                                        JpaLevelRepository jpaLevelRepository,
                                        JpaSchoolRepository jpaSchoolRepository,
                                        JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository,
                                        JpaCourseRepository jpaCourseRepository,
                                        JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository,
                                        JpaStudentOutletLinkRepository jpaStudentOutletLinkRepository,
                                        JpaSubjectStudentLinkRepository jpaSubjectStudentLinkRepository,
                                        JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository,
                                        EntityManager entityManager) {
        return new StudentRepositoryImpl(jpaStudentRepository, jpaLevelRepository, jpaSchoolRepository,
                jpaStudentCourseLinkRepository, jpaCourseRepository, jpaStudentLessonLinkRepository,
                jpaStudentOutletLinkRepository, jpaSubjectStudentLinkRepository, jpaAccountStudentLinkRepository,
                entityManager);
    }

    @Bean
    EducatorRepository educatorRepository(JpaEducatorRepository jpaEducatorRepository, JpaCourseRepository jpaCourseRepository,
                                          JpaOutletRepository jpaOutletRepository,
                                          JpaLevelRepository jpaLevelRepository,
                                          JpaSubjectRepository jpaSubjectRepository,
                                          JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository,
                                          JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository,
                                          JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository,
                                          JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository,
                                          JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository,
                                          JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository) {
        return new EducatorRepositoryImpl(jpaEducatorRepository, jpaCourseRepository, jpaOutletRepository, jpaLevelRepository,
                jpaSubjectRepository, jpaEducatorCourseLinkRepository, jpaOutletEducatorLinkRepository, jpaLevelEducatorLinkRepository, jpaSubjectEducatorLinkRepository,
                jpaEducatorLessonLinkRepository, jpaAccountEducatorLinkRepository);
    }

    @Bean
    OutletRepository outletRepository(JpaOutletRepository jpaRepository,
                                      JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository) {
        return new OutletRepositoryImpl(jpaRepository, jpaOutletEducatorLinkRepository);
    }

    @Bean
    OutletRoomRepository outletRoomRepository(JpaOutletRoomRepository jpaOutletRoomRepository,
                                              EntityManager entityManager) {
        return new OutletRoomRepositoryImpl(jpaOutletRoomRepository, entityManager);
    }

    @Bean
    CourseRepository courseRepository(JpaCourseRepository jpaRepository,
                                      JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository,
                                      JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository,
                                      EntityManager entityManager) {
        return new CourseRepositoryImpl(jpaRepository, jpaStudentCourseLinkRepository, jpaEducatorCourseLinkRepository, entityManager);
    }

    @Bean
    LessonRepository lessonRepository(JpaLessonRepository jpaRepository,
                                      JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository,
                                      JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository,
                                      JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository,
                                      JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository,
                                      JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository,
                                      EntityManager entityManager) {
        return new LessonRepositoryImpl(jpaRepository, jpaLevelLessonLinkRepository, jpaSubjectLessonLinkRepository,
                jpaTopicLessonLinkRepository, jpaStudentLessonLinkRepository, jpaEducatorLessonLinkRepository, entityManager);
    }

    @Bean
    LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository(JpaLessonOutletRoomBookingRepository jpaRepository,
                                                                        EntityManager entityManager) {
        return new LessonOutletRoomBookingRepositoryImpl(jpaRepository, entityManager);
    }

    @Bean
    LessonGroupRepository lessonGroupRepository(JpaLessonGroupRepository jpaLessonGroupRepository,
                                                EntityManager entityManager) {
        return new LessonGroupRepositoryImpl(jpaLessonGroupRepository, entityManager);
    }

    @Bean
    LessonGroupPlanRepository lessonGroupPlanRepository(JpaLessonGroupPlanRepository jpaLessonGroupPlanRepository,
                                                        EntityManager entityManager) {
        return new LessonGroupPlanRepositoryImpl(jpaLessonGroupPlanRepository, entityManager);
    }

    @Bean
    LessonGroupPlanMaterialLinkRepository lessonGroupPlanMaterialLinkRepository(JpaLessonGroupPlanMaterialsLinkRepository jpaLessonGroupPlanMaterialsLinkRepository,
                                                                                EntityManager entityManager) {
        return new LessonGroupPlanMaterialLinkRepositoryImpl(jpaLessonGroupPlanMaterialsLinkRepository, entityManager);
    }

    @Bean
    LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository(JpaLessonGroupPlanTopicLinkRepository jpaLessonGroupPlanTopicLinkRepository,
                                                                          EntityManager entityManager) {
        return new LessonGroupPlanTopicLinkRepositoryImpl(jpaLessonGroupPlanTopicLinkRepository, entityManager);
    }

    @Bean
    PriceRecordRepository priceRecordRepository(JpaPriceRecordRepository jpaRepository, EntityManager entityManager) {
        return new PriceRecordRepositoryImpl(jpaRepository, entityManager);
    }

    @Bean
    LessonObjectiveRepository lessonObjectiveRepository(JpaLessonObjectiveRepository jpaLessonObjectiveRepository,
                                                        JpaLessonObjectiveTopicLinkRepository jpaLessonObjectiveTopicLinkRepository,
                                                        EntityManager entityManager) {
        return new LessonObjectiveRepositoryImpl(jpaLessonObjectiveRepository, jpaLessonObjectiveTopicLinkRepository, entityManager);
    }

    @Bean
    LessonObjectiveTopicLinkRepository lessonObjectiveTopicLinkRepository(JpaLessonObjectiveTopicLinkRepository jpaLessonObjectiveTopicLinkRepository,
                                                                          EntityManager entityManager) {
        return new LessonObjectiveTopicLinkRepositoryImpl(jpaLessonObjectiveTopicLinkRepository, entityManager);
    }

    @Bean
    SchoolRepository schoolRepository(JpaSchoolRepository jpaSchoolRepository) {
        return new SchoolRepositoryImpl(jpaSchoolRepository);
    }

    @Bean
    TopicRepository topicRepository(JpaTopicRepository jpaRepository,
                                    JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository,
                                    JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository) {
        return new TopicRepositoryImpl(jpaRepository, jpaTopicSubjectLinkRepository, jpaTopicLessonLinkRepository);
    }

    @Bean
    SubjectRepository subjectRepository(JpaSubjectRepository jpaSubjectRepository,
                                        JpaTopicRepository jpaTopicRepository,
                                        JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository,
                                        JpaCourseRepository jpaCourseRepository,
                                        JpaSubjectCourseLinkRepository jpaSubjectCourseLinkRepository,
                                        JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository,
                                        JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository) {
        return new SubjectRepositoryImpl(jpaSubjectRepository, jpaTopicRepository, jpaTopicSubjectLinkRepository,
                jpaCourseRepository, jpaSubjectCourseLinkRepository, jpaSubjectLessonLinkRepository, jpaSubjectEducatorLinkRepository);
    }

    @Bean
    EducatorLessonLinkRepository educatorLessonLinkRepository(JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository, EntityManager entityManager) {
        return new EducatorLessonLinkRepositoryImpl(jpaEducatorLessonLinkRepository, entityManager);
    }

    @Bean
    StudentLessonLinkRepository studentLessonLinkRepository(JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository, EntityManager entityManager) {
        return new StudentLessonLinkRepositoryImpl(jpaStudentLessonLinkRepository, entityManager);
    }

    @Bean
    StudentLessonReportRepository studentLessonReportRepository(JpaStudentLessonReportRepository jpaStudentLessonReportRepository, EntityManager entityManager) {
        return new StudentLessonReportRepositoryImpl(jpaStudentLessonReportRepository, entityManager);
    }

    @Bean
    SubjectLessonLinkRepository subjectLessonLinkRepository(JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository, EntityManager entityManager) {
        return new SubjectLessonLinkRepositoryImpl(jpaSubjectLessonLinkRepository, entityManager);
    }

    @Bean
    TopicLessonLinkRepository topicLessonLinkRepository(JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository, EntityManager entityManager) {
        return new TopicLessonLinkRepositoryImpl(jpaTopicLessonLinkRepository, entityManager);
    }

    @Bean
    LevelLessonLinkRepository levelLessonLinkRepository(JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository, EntityManager entityManager) {
        return new LevelLessonLinkRepositoryImpl(jpaLevelLessonLinkRepository, entityManager);
    }

    @Bean
    EmailLogRepository emailLogRepository(JpaEmailLogRepository jpaEmailLogRepository) {
        return new EmailLogRepositoryImpl(jpaEmailLogRepository);
    }

    @Bean
    MaterialRepository materialRepository(JpaMaterialRepository jpaMaterialRepository,
                                          JpaTopicRepository jpaTopicRepository,
                                          JpaMaterialTopicLinkRepository jpaMaterialTopicLinkRepository,
                                          JpaLessonGroupPlanMaterialsLinkRepository jpaLessonGroupPlanMaterialsLinkRepository) {
        return new MaterialRepositoryImpl(jpaMaterialRepository, jpaTopicRepository, jpaMaterialTopicLinkRepository,
                jpaLessonGroupPlanMaterialsLinkRepository);
    }
}
