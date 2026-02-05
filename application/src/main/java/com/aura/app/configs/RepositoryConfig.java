package com.aura.app.configs;

import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.SchoolRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.details.springdata.repositories.impl.AccountRepositoryImpl;
import com.aura.details.springdata.repositories.impl.CourseRepositoryImpl;
import com.aura.details.springdata.repositories.impl.EducatorRepositoryImpl;
import com.aura.details.springdata.repositories.impl.InstitutionRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LessonRepositoryImpl;
import com.aura.details.springdata.repositories.impl.LevelRepositoryImpl;
import com.aura.details.springdata.repositories.impl.OutletRepositoryImpl;
import com.aura.details.springdata.repositories.impl.SchoolRepositoryImpl;
import com.aura.details.springdata.repositories.impl.StudentRepositoryImpl;
import com.aura.details.springdata.repositories.impl.SubjectRepositoryImpl;
import com.aura.details.springdata.repositories.impl.TopicRepositoryImpl;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    InstitutionRepository institutionRepository(JpaInstitutionRepository jpaRepository) {
        return new InstitutionRepositoryImpl(jpaRepository);
    }

    @Bean
    LevelRepository levelRepository(JpaLevelRepository jpaRepository) {
        return new LevelRepositoryImpl(jpaRepository);
    }

    @Bean
    AccountRepository accountRepository(JpaAccountRepository jpaAccountRepository, JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository, JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository, JpaStudentRepository jpaStudentRepository, JpaEducatorRepository jpaEducatorRepository) {
        return new AccountRepositoryImpl(jpaAccountRepository, jpaAccountStudentLinkRepository, jpaAccountEducatorLinkRepository, jpaStudentRepository, jpaEducatorRepository);
    }

    @Bean
    StudentRepository studentRepository(JpaStudentRepository jpaStudentRepository, JpaLevelRepository jpaLevelRepository, JpaSchoolRepository jpaSchoolRepository, JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository, JpaCourseRepository jpaCourseRepository) {
        return new StudentRepositoryImpl(jpaStudentRepository, jpaLevelRepository, jpaSchoolRepository, jpaStudentCourseLinkRepository, jpaCourseRepository);
    }

    @Bean
    EducatorRepository educatorRepository(JpaEducatorRepository jpaEducatorRepository, JpaCourseRepository jpaCourseRepository,
                                          JpaOutletRepository jpaOutletRepository,
                                          JpaLevelRepository jpaLevelRepository,
                                          JpaSubjectRepository jpaSubjectRepository,
                                          JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository,
                                          JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository,
                                          JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository,
                                          JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository) {
        return new EducatorRepositoryImpl(jpaEducatorRepository, jpaCourseRepository, jpaOutletRepository, jpaLevelRepository,
                jpaSubjectRepository, jpaEducatorCourseLinkRepository, jpaOutletEducatorLinkRepository, jpaLevelEducatorLinkRepository, jpaSubjectEducatorLinkRepository);
    }

    @Bean
    OutletRepository outletRepository(JpaOutletRepository jpaRepository) {
        return new OutletRepositoryImpl(jpaRepository);
    }

    @Bean
    CourseRepository courseRepository(JpaCourseRepository jpaRepository) {
        return new CourseRepositoryImpl(jpaRepository);
    }

    @Bean
    LessonRepository lessonRepository(JpaLessonRepository jpaRepository) {
        return new LessonRepositoryImpl(jpaRepository);
    }

    @Bean
    SchoolRepository schoolRepository(JpaSchoolRepository jpaSchoolRepository) {
        return new SchoolRepositoryImpl(jpaSchoolRepository);
    }

    @Bean
    TopicRepository topicRepository(JpaTopicRepository jpaRepository) {
        return new TopicRepositoryImpl(jpaRepository);
    }

    @Bean
    SubjectRepository subjectRepository(JpaSubjectRepository jpaSubjectRepository) {
        return new SubjectRepositoryImpl(jpaSubjectRepository);
    }
}
