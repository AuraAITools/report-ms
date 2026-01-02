package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;

public class CreateOutletUseCaseImpl implements CreateOutletUseCase {
    private final OutletRepository outletRepository;

    public CreateOutletUseCaseImpl(OutletRepository outletRepository) {
        this.outletRepository = outletRepository;
    }

    @Override
    public Outlet createOutlet(Outlet outlet) {
        return outletRepository.save(outlet);
    }
}
