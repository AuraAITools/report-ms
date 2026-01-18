package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateStudentInput;
import com.aura.app.codegen.types.StudentCourseEnrollmentInput;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.core.entities.students.Student;
import com.aura.details.services.AccountService;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, CourseMapper.class, LevelMapper.class, SchoolMapper.class})
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "studentOutletLinks", ignore = true)
    @Mapping(target = "studentLessonLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "level", ignore = true)
    Student toDomain(CreateStudentInput createStudentInput, String tenantId);

    @Mapping(target = "studentCourseEnrollmentParams", expression = "java(toParams(createStudentInput.getCourseEnrollments()))")
    @Mapping(target = "student", expression = "java(toDomain(createStudentInput, tenantId))")
    AccountService.CreateStudentAccountParams toParams(CreateStudentInput createStudentInput, String tenantId);

    List<AccountService.CreateStudentCourseEnrollmentParams> toParams(List<StudentCourseEnrollmentInput> studentCourseEnrollmentInputs);

    @Mapping(target = "enrollmentDateTime", source = "startDate")
    AccountService.CreateStudentCourseEnrollmentParams toParams(StudentCourseEnrollmentInput studentCourseEnrollmentInput);

    @Mapping(target = "updatedBy", source = "student.updatedBy")
    @Mapping(target = "updatedAt", source = "student.updatedAt")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "email", source = "student.email")
    @Mapping(target = "dateOfBirth", source = "student.dateOfBirth")
    @Mapping(target = "createdBy", source = "student.createdBy")
    @Mapping(target = "createdAt", source = "student.createdAt")
    @Mapping(target = "level", source = "level") // INFO: mapped by uses
    @Mapping(target = "school", source = "school")
    @Mapping(target = "courses", source = "courses")
    StudentResponse toDto(AccountService.CreatedStudentResult createdStudentResult);


}
