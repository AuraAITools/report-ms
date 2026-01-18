package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.EducatorRepository;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.mappers.outlets.OutletMapper;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.tables.courses.CourseTable;
import com.aura.details.springdata.tables.educators.EducatorTable;
import com.aura.details.springdata.tables.levels.LevelTable;
import com.aura.details.springdata.tables.links.EducatorCourseLinkTable;
import com.aura.details.springdata.tables.links.LevelEducatorLinkTable;
import com.aura.details.springdata.tables.links.OutletEducatorLinkTable;
import com.aura.details.springdata.tables.links.SubjectEducatorLinkTable;
import com.aura.details.springdata.tables.outlets.OutletTable;
import com.aura.details.springdata.tables.subjects.SubjectTable;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class EducatorRepositoryImpl implements EducatorRepository {
    private final JpaEducatorRepository jpaEducatorRepository;
    private final JpaCourseRepository jpaCourseRepository;
    private final JpaOutletRepository jpaOutletRepository;
    private final JpaLevelRepository jpaLevelRepository;
    private final JpaSubjectRepository jpaSubjectRepository;
    private final JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository;
    private final JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository;
    private final JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository;
    private final JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository;

    public EducatorRepositoryImpl(JpaEducatorRepository jpaEducatorRepository, JpaCourseRepository jpaCourseRepository, JpaOutletRepository jpaOutletRepository, JpaLevelRepository jpaLevelRepository, JpaSubjectRepository jpaSubjectRepository, JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository, JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository, JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository, JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository) {
        this.jpaEducatorRepository = jpaEducatorRepository;
        this.jpaCourseRepository = jpaCourseRepository;
        this.jpaOutletRepository = jpaOutletRepository;
        this.jpaLevelRepository = jpaLevelRepository;
        this.jpaSubjectRepository = jpaSubjectRepository;
        this.jpaEducatorCourseLinkRepository = jpaEducatorCourseLinkRepository;
        this.jpaOutletEducatorLinkRepository = jpaOutletEducatorLinkRepository;
        this.jpaLevelEducatorLinkRepository = jpaLevelEducatorLinkRepository;
        this.jpaSubjectEducatorLinkRepository = jpaSubjectEducatorLinkRepository;
    }

    @Override
    public @Nullable Educator findById(String id) {
        return jpaEducatorRepository.findById(UUID.fromString(id)).map(EducatorMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public Educator save(Educator educator) {
        EducatorTable educatorTable = EducatorMapper.INSTANCE.toTableEntity(educator);
        EducatorTable createdEducatorTable = jpaEducatorRepository.saveAndFlush(educatorTable);
        return EducatorMapper.INSTANCE.toDomainEntity(createdEducatorTable);
    }

    @Override
    public Course createEducatorCourseLink(String educatorId, String courseId, String tenantId) {
        CourseTable courseTable = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        EducatorTable educatorTable = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        EducatorCourseLinkTable educatorCourseLinkTable = EducatorCourseLinkTable
                .builder()
                .course(courseTable)
                .educator(educatorTable)
                .tenantId(tenantId)
                .build();
        jpaEducatorCourseLinkRepository.save(educatorCourseLinkTable);
        return CourseMapper.INSTANCE.toDomainEntity(courseTable);
    }

    @Override
    public Outlet createOutletEducatorLink(String educatorId, String outletId, String tenantId) {
        OutletTable outletTable = jpaOutletRepository.getReferenceById(UUID.fromString(outletId));
        EducatorTable educatorTable = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        OutletEducatorLinkTable outletEducatorLinkTable = OutletEducatorLinkTable
                .builder()
                .outlet(outletTable)
                .educator(educatorTable)
                .tenantId(tenantId)
                .build();
        jpaOutletEducatorLinkRepository.save(outletEducatorLinkTable);
        return OutletMapper.INSTANCE.toDomainEntity(outletTable);
    }

    @Override
    public Level createLevelEducatorLink(String educatorId, String levelId, String tenantId) {
        LevelTable levelTable = jpaLevelRepository.getReferenceById(UUID.fromString(levelId));
        EducatorTable educatorTable = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        LevelEducatorLinkTable levelEducatorLinkTable = LevelEducatorLinkTable
                .builder()
                .level(levelTable)
                .educator(educatorTable)
                .tenantId(tenantId)
                .build();
        jpaLevelEducatorLinkRepository.save(levelEducatorLinkTable);
        return LevelMapper.INSTANCE.toDomainEntity(levelTable);
    }

    @Override
    public Subject createSubjectEducatorLink(String educatorId, String subjectId, String tenantId) {
        SubjectTable subjectTable = jpaSubjectRepository.getReferenceById(UUID.fromString(subjectId));
        EducatorTable educatorTable = jpaEducatorRepository.getReferenceById(UUID.fromString(educatorId));
        SubjectEducatorLinkTable subjectEducatorLinkTable = SubjectEducatorLinkTable
                .builder()
                .subject(subjectTable)
                .educator(educatorTable)
                .tenantId(tenantId)
                .build();
        jpaSubjectEducatorLinkRepository.save(subjectEducatorLinkTable);
        return SubjectMapper.INSTANCE.toDomainEntity(subjectTable);
    }
}
