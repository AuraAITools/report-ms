package com.reportai.www.reportapi.dtos.requests;

import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Institution;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CreateInstitutionRequestBody extends DTOSupport<Institution> {
    private CreateUserRequestBody user;

    public CreateInstitutionRequestBody(ModelMapper modelMapper, CreateUserRequestBody user) {
        super(modelMapper);
        this.user = user;
    }

    // TODO: implement
    @Override
    public Institution toEntity() {
        return null;
    }
}
