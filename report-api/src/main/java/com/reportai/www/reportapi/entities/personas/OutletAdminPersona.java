package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Outlet;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "OutletAdminPersonas")
public class OutletAdminPersona extends Persona {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "outlet_admin_id"),
            inverseJoinColumns = @JoinColumn(name = "outlet_id")
    )
    @Builder.Default
    @ToString.Exclude
    public Set<Outlet> outlets = new HashSet<>();


}
