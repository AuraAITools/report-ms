package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.educators.Educator;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * a EducatorsClient is able to access the report-mobile's edycators feature
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "EducatorClientPersonas")
public class EducatorClientPersona extends Persona {


    @OneToOne(mappedBy = "educatorClientPersona")
    @ToString.Exclude
    private Educator educator;

}
