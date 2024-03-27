package com.reportai.www.reportapi.dtos.responses;

import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.dtos.responses.commons.ParentDTO;
import com.reportai.www.reportapi.dtos.responses.commons.UserDTO;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.transaction.NotSupportedException;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ParentResponseDTO extends ParentDTO {

    public ParentResponseDTO(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Parent toEntity() {
        return null;
    }

    private UserDTO user;

//
//    private List<Student> students;
//
//    private List<Institution> institutions;

}
