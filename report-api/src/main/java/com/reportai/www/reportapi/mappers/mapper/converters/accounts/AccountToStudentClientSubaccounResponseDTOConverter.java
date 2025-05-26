package com.reportai.www.reportapi.mappers.mapper.converters.accounts;

import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedAccountResponse;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.attachments.AccountStudentAttachment;
import com.reportai.www.reportapi.mappers.mapper.converters.students.StudentToStudentResponseDTOConverter;
import java.util.List;
import java.util.Objects;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountToStudentClientSubaccounResponseDTOConverter implements Converter<Account, ExpandedAccountResponse.StudentClientSubaccountResponseDTO> {

    private final StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter;

    @Autowired
    public AccountToStudentClientSubaccounResponseDTOConverter(StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter) {
        this.studentToStudentResponseDTOConverter = studentToStudentResponseDTOConverter;
    }

    @Override
    public ExpandedAccountResponse.StudentClientSubaccountResponseDTO convert(MappingContext<Account, ExpandedAccountResponse.StudentClientSubaccountResponseDTO> mappingContext) {
        Objects.requireNonNull(mappingContext.getSource(), "Source account cannot be null");

        List<StudentResponseDTO> studentDTOs = mappingContext
                .getSource()
                .getAccountStudentAttachments()
                .stream()
                .map(AccountStudentAttachment::getStudent)
                .map(student -> studentToStudentResponseDTOConverter.convert(
                        mappingContext.create(student, StudentResponseDTO.class)
                ))
                .toList();

        return ExpandedAccountResponse.StudentClientSubaccountResponseDTO
                .builder()
                .relationship(mappingContext.getSource().getRelationship())
                .students(studentDTOs)
                .build();
    }

}
