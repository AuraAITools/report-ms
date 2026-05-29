package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateOutletUseCaseImpl implements UpdateOutletUseCase {
    private final OutletRepository outletRepository;

    @Override
    public @Nullable Outlet updateOutlet(Outlet outlet) {
        Outlet existing = outletRepository.findById(outlet.getId());
        if (existing == null || !existing.getTenantId().equals(outlet.getTenantId())) {
            return null;
        }
        existing.setName(outlet.getName());
        existing.setAddress(outlet.getAddress());
        existing.setPostalCode(outlet.getPostalCode());
        existing.setContactNumber(outlet.getContactNumber());
        existing.setDescription(outlet.getDescription());
        existing.setEmail(outlet.getEmail());
        return outletRepository.save(existing);
    }
}
