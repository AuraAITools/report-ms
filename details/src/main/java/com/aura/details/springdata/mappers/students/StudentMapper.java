package com.aura.details.springdata.mappers.students;

import com.aura.core.entities.students.Student;
import com.aura.details.springdata.models.students.StudentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "school.id", target = "schoolId")
    @Mapping(source = "level.id", target = "levelId")
    Student toDomainEntity(StudentModel studentModel);

    // school and level are attached separately by StudentRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "level", ignore = true)
    StudentModel toModelEntity(Student student);
}
