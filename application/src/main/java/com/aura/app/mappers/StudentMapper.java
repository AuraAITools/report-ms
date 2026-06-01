package com.aura.app.mappers;

import com.aura.app.codegen.types.AccountStudentDetailsInput;
import com.aura.app.codegen.types.CreateParentAndStudentsInAccountInput;
import com.aura.app.codegen.types.CreateStudentInput;
import com.aura.app.codegen.types.StudentCourseEnrollmentInput;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.core.entities.students.Student;
import com.aura.details.services.AccountService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, CourseMapper.class, LevelMapper.class, SchoolMapper.class, AccountMinimalDetailMapper.class})
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "schoolId", ignore = true)
    @Mapping(target = "levelId", ignore = true)
    @Mapping(target = "tenantId", source = "institutionId")
    Student toDomain(CreateStudentInput createStudentInput);

    @Mapping(target = "studentCourseEnrollmentParams", expression = "java(toParams(createStudentInput.getCourseEnrollments()))")
    @Mapping(target = "student", expression = "java(toDomain(createStudentInput))")
    AccountService.CreateStudentParams toParams(CreateStudentInput createStudentInput);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "schoolId", ignore = true)
    @Mapping(target = "levelId", ignore = true)
    @Mapping(target = "tenantId", source = "institutionId")
    @Mapping(target = "name", source = "input.name")
    @Mapping(target = "email", source = "input.email")
    @Mapping(target = "dateOfBirth", source = "input.dateOfBirth")
    Student toDomain(AccountStudentDetailsInput input, String institutionId);

    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "tenantId", source = "institutionId")
    @Mapping(target = "levelId", source = "input.levelId")
    @Mapping(target = "schoolId", source = "input.schoolId")
    @Mapping(target = "student", expression = "java(toDomain(input, institutionId))")
    @Mapping(target = "studentCourseEnrollmentParams", expression = "java(toParams(input.getCourseEnrollments()))")
    AccountService.CreateStudentParams toParams(AccountStudentDetailsInput input, String accountId, String institutionId);

    default List<AccountService.CreateStudentParams> toParams(CreateParentAndStudentsInAccountInput input) {
        return input.getStudents().stream()
                .map(s -> toParams(s, input.getAccountId(), input.getInstitutionId()))
                .toList();
    }

    List<AccountService.CreateStudentCourseEnrollmentParams> toParams(List<StudentCourseEnrollmentInput> studentCourseEnrollmentInputs);

    @Mapping(target = "enrollmentDateTime", source = "startDate")
    AccountService.CreateStudentCourseEnrollmentParams toParams(StudentCourseEnrollmentInput studentCourseEnrollmentInput);

    @Mapping(target = "updatedBy", source = "student.updatedBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedAt", source = "student.updatedAt")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "email", source = "student.email")
    @Mapping(target = "dateOfBirth", source = "student.dateOfBirth")
    @Mapping(target = "createdBy", source = "student.createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "createdAt", source = "student.createdAt")
    @Mapping(target = "level", source = "level") // INFO: mapped by uses
    @Mapping(target = "school", source = "school")
    @Mapping(target = "courses", source = "courses")
    @Mapping(target = "profileImageUrl", ignore = true)
    StudentResponse toDto(AccountService.CreatedStudentResult createdStudentResult);

    @Mapping(target = "level", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    StudentResponse toDto(Student student);

}
