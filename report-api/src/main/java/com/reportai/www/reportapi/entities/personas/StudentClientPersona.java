package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * a Students Client is able to access the report-mobile's students analytics dashboard
 */

@Getter
@Setter
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
    @Builder.Default
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();
}
