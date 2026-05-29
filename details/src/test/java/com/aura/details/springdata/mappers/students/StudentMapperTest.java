package com.aura.details.springdata.mappers.students;

import com.aura.core.entities.students.Student;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StudentMapper")
class StudentMapperTest {

    private final StudentMapper mapper = StudentMapper.INSTANCE;

    @DisplayName("Should map StudentModel.school.id into Student.schoolId without traversing back into School")
    @Test
    void toDomainEntity_extractsSchoolIdFromModel() {
        UUID schoolId = UUID.randomUUID();
        SchoolModel schoolModel = SchoolModel.builder().id(schoolId).build();
        StudentModel model = StudentModel.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .school(schoolModel)
                .build();

        Student domain = mapper.toDomainEntity(model);

        assertThat(domain.getSchoolId()).isEqualTo(schoolId.toString());
        assertThat(domain.getName()).isEqualTo("Alice");
    }

    @DisplayName("Should map StudentModel.level.id into Student.levelId without traversing back into Level")
    @Test
    void toDomainEntity_extractsLevelIdFromModel() {
        UUID levelId = UUID.randomUUID();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        StudentModel model = StudentModel.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .level(levelModel)
                .build();

        Student domain = mapper.toDomainEntity(model);

        assertThat(domain.getLevelId()).isEqualTo(levelId.toString());
        assertThat(domain.getName()).isEqualTo("Alice");
    }

    @DisplayName("Should leave StudentModel.school null on toModelEntity — StudentRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesSchoolNullForRepositoryImplToAttach() {
        Student domain = Student.builder()
                .id(UUID.randomUUID().toString())
                .schoolId(UUID.randomUUID().toString())
                .name("Alice")
                .build();

        StudentModel model = mapper.toModelEntity(domain);

        assertThat(model.getSchool()).isNull();
        assertThat(model.getName()).isEqualTo("Alice");
    }

    @DisplayName("Should leave StudentModel.level null on toModelEntity — StudentRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLevelNullForRepositoryImplToAttach() {
        Student domain = Student.builder()
                .id(UUID.randomUUID().toString())
                .levelId(UUID.randomUUID().toString())
                .name("Alice")
                .build();

        StudentModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNull();
        assertThat(model.getName()).isEqualTo("Alice");
    }

    @DisplayName("Should preserve non-association fields across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesNonAssociationFields() {
        UUID id = UUID.randomUUID();
        StudentModel original = StudentModel.builder()
                .id(id)
                .name("Alice")
                .build();

        StudentModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(id);
        assertThat(roundTripped.getName()).isEqualTo("Alice");
    }
}
