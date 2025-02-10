package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * a Students Client is able to access the report-mobile's students analytics dashboard
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "StudentClientPersonas")
public class StudentClientPersona extends Persona {
    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;


    @OneToMany(mappedBy = "studentClientPersona")
    private List<Student> students;
}
