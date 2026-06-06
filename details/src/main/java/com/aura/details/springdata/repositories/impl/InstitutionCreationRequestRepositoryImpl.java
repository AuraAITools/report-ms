package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.repositories.InstitutionCreationRequestRepository;
import com.aura.details.springdata.mappers.institutions.InstitutionCreationRequestMapper;
import com.aura.details.springdata.models.institutions.InstitutionCreationRequestModel;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionCreationRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class InstitutionCreationRequestRepositoryImpl implements InstitutionCreationRequestRepository {

    private final JpaInstitutionCreationRequestRepository jpaRepository;

    @Override
    public InstitutionCreationRequest save(InstitutionCreationRequest request) {
        InstitutionCreationRequestModel model = InstitutionCreationRequestMapper.INSTANCE.toModelEntity(request);
        InstitutionCreationRequestModel saved = jpaRepository.save(model);
        return InstitutionCreationRequestMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable InstitutionCreationRequest findById(String id) {
        Optional<InstitutionCreationRequestModel> model = jpaRepository.findById(UUID.fromString(id));
        return model.map(InstitutionCreationRequestMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<InstitutionCreationRequest> findAll() {
        return jpaRepository.findAll().stream()
                .map(InstitutionCreationRequestMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByInstitutionNameAndState(String institutionName, InstitutionCreationRequest.STATE state) {
        return jpaRepository.existsByInstitutionNameAndState(
                institutionName, InstitutionCreationRequestModel.STATE.valueOf(state.name()));
    }

    @Override
    public boolean existsByInstitutionEmailAndState(String institutionEmail, InstitutionCreationRequest.STATE state) {
        return jpaRepository.existsByInstitutionEmailAndState(
                institutionEmail, InstitutionCreationRequestModel.STATE.valueOf(state.name()));
    }
}
