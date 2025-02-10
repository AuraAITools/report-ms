package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Outlet;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "OutletAdminPersonas")
public class OutletAdminPersona extends Persona {

    @ManyToMany(mappedBy = "outletAdminPersonas", fetch = FetchType.LAZY)
    public List<Outlet> outlets;

}
