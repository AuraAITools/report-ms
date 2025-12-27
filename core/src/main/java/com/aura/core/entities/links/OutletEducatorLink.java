package com.aura.core.entities.links;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.outlets.Outlet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OutletEducatorLink {

    private Educator educator;

    private Outlet outlet;

}
