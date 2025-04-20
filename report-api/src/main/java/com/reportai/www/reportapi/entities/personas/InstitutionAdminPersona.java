package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Institution;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Audited
@Table(name = "InstitutionAdminPersonas")
public class InstitutionAdminPersona extends Persona {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Institution institution;
}
