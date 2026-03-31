package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateOutletUseCaseImpl implements CreateOutletUseCase {
    private final OutletRepository outletRepository;

    @Override
    public Outlet createOutlet(Outlet outlet) {
        return outletRepository.save(outlet);
    }
}
