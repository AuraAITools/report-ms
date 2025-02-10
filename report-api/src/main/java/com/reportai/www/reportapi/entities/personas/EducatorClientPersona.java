package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Educator;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * a EducatorsClient is able to access the report-mobile's edycators feature
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "EducatorClientPersonas")
public class EducatorClientPersona extends Persona {


    @OneToOne(mappedBy = "educatorClientPersona")
    private Educator educator;

}
