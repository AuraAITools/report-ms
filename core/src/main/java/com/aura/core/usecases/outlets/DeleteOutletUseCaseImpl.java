package com.aura.core.usecases.outlets;

import com.aura.core.repositories.OutletRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteOutletUseCaseImpl implements DeleteOutletUseCase {
    private final OutletRepository outletRepository;

    @Override
    public void deleteById(String id) {
        outletRepository.deleteById(id);
    }
}
