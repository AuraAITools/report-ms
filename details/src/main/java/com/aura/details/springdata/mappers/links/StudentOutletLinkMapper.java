package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentOutletLink;
import com.aura.details.springdata.models.links.StudentOutletLinkModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentOutletLinkMapper {

    StudentOutletLinkMapper INSTANCE = Mappers.getMapper(StudentOutletLinkMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "outlet.id", target = "outletId")
    StudentOutletLink toDomain(StudentOutletLinkModel model);

    @Mapping(source = "studentId", target = "student", qualifiedByName = "studentRef")
    @Mapping(source = "outletId", target = "outlet", qualifiedByName = "outletRef")
    StudentOutletLinkModel toTable(StudentOutletLink link);

    @Named("studentRef")
    default StudentModel studentRef(String studentId) {
        if (studentId == null) {
            return null;
        }
        return StudentModel.builder().id(UUID.fromString(studentId)).build();
    }

    @Named("outletRef")
    default OutletModel outletRef(String outletId) {
        if (outletId == null) {
            return null;
        }
        return OutletModel.builder().id(UUID.fromString(outletId)).build();
    }
}
