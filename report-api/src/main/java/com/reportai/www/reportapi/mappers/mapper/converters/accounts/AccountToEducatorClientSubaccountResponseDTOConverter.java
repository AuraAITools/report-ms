package com.reportai.www.reportapi.mappers.mapper.converters.accounts;

import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedAccountResponse;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachment;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class AccountToEducatorClientSubaccountResponseDTOConverter implements Converter<Account, ExpandedAccountResponse.EducatorClientSubaccountResponseDTO>, ModelMapperConfigurer {
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedAccountResponse.EducatorClientSubaccountResponseDTO convert(MappingContext<Account, ExpandedAccountResponse.EducatorClientSubaccountResponseDTO> mappingContext) {
        List<EducatorResponseDTO> educatorResponseDTOS = mappingContext
                .getSource()
                .getAccountEducatorAttachments()
                .stream()
                .map(AccountEducatorAttachment::getEducator)
                .map(educator -> mappingContext.getMappingEngine().map(
                        mappingContext.create(educator, EducatorResponseDTO.class))) // INFO: use the default model mapping mapping
                .toList();
        return ExpandedAccountResponse.EducatorClientSubaccountResponseDTO
                .builder()
                .educators(educatorResponseDTOS)
                .build();
    }
}
