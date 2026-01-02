package com.aura.app.controllers.graphql.v1.outlets;

import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.mappers.OutletMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.outlets.Outlet;
import com.aura.details.services.OutletService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEOUTLET_INPUT_ARGUMENT.CreateOutletInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateOutlet;

@Controller
public class OutletsGqlController {
    private final OutletService outletService;

    public OutletsGqlController(OutletService outletService) {
        this.outletService = outletService;
    }

    @MutationMapping(CreateOutlet)
    public OutletResponse createOutlet(@Argument(name = CreateOutletInput) CreateOutletInput createOutletInput, @ContextValue(name = "user") User user) {
        Outlet outletToBeCreated = OutletMapper.INSTANCE.toDomainEntity(createOutletInput, user.getCurrentTenantAccessed());
        Outlet outlet = outletService.createOutlet(outletToBeCreated);
        return OutletMapper.INSTANCE.toDto(outlet);
    }
    
}
