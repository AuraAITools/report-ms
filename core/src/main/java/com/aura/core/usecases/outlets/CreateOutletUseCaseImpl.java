package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.exceptions.DuplicateResourceException;
import com.aura.core.repositories.OutletRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateOutletUseCaseImpl implements CreateOutletUseCase {
    private final OutletRepository outletRepository;

    @Override
    public Outlet createOutlet(Outlet outlet) {
        if (outletRepository.existsByNameAndTenantId(outlet.getName(), outlet.getTenantId())) {
            throw new DuplicateResourceException(
                    "Outlet with name '%s' already exists in this institution".formatted(outlet.getName()));
        }
        return outletRepository.save(outlet);
    }
}
