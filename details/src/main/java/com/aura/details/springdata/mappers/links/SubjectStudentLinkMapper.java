package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectStudentLink;
import com.aura.details.springdata.models.links.SubjectStudentLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectStudentLinkMapper {
    SubjectStudentLinkMapper INSTANCE = Mappers.getMapper(SubjectStudentLinkMapper.class);

    @Mapping(source = "studentId", target = "student", qualifiedByName = "studentRef")
    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "subjectRef")
    SubjectStudentLinkModel toModelEntity(SubjectStudentLink domain);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    SubjectStudentLink toDomain(SubjectStudentLinkModel table);

    @Named("studentRef")
    default StudentModel studentRef(String studentId) {
        if (studentId == null) {
            return null;
        }
        return StudentModel.builder().id(UUID.fromString(studentId)).build();
    }

    @Named("subjectRef")
    default SubjectModel subjectRef(String subjectId) {
        if (subjectId == null) {
            return null;
        }
        return SubjectModel.builder().id(UUID.fromString(subjectId)).build();
    }
}
