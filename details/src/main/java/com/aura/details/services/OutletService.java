package com.aura.details.services;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.outlets.CreateOutletUseCase;

public class OutletService {
    private final CreateOutletUseCase createOutletUseCase;

    public OutletService(CreateOutletUseCase createOutletUseCase) {
        this.createOutletUseCase = createOutletUseCase;
    }

    public Outlet createOutlet(Outlet outlet) {
        return createOutletUseCase.createOutlet(outlet);
    }
}
