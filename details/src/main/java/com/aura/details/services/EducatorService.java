package com.aura.details.services;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.educators.Educator.EMPLOYMENT_TYPE;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.usecases.educators.DeleteEducatorUseCase;
import com.aura.core.usecases.educators.EducatorQueryUseCase;
import com.aura.core.usecases.educators.UpdateEducatorUseCase;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class EducatorService {
    private final EducatorQueryUseCase educatorQueryUseCase;
    private final UpdateEducatorUseCase updateEducatorUseCase;
    private final DeleteEducatorUseCase deleteEducatorUseCase;
    private final EducatorRepository educatorRepository;

    public @Nullable Educator findById(String id) {
        return educatorQueryUseCase.findById(id);
    }

    public List<Educator> findAllByTenantId(String tenantId) {
        return educatorQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public Educator updateById(String id,
                               @Nullable String name,
                               @Nullable LocalDate startDate,
                               @Nullable LocalDate dateOfBirth,
                               @Nullable String email,
                               @Nullable EMPLOYMENT_TYPE employmentType) {
        return updateEducatorUseCase.update(id, name, startDate, dateOfBirth, email, employmentType);
    }

    @Transactional
    public void deleteById(String id) {
        educatorRepository.deleteAllEducatorCourseLinksByEducatorId(id);
        educatorRepository.deleteAllEducatorLessonLinksByEducatorId(id);
        educatorRepository.deleteAllLevelEducatorLinksByEducatorId(id);
        educatorRepository.deleteAllOutletEducatorLinksByEducatorId(id);
        educatorRepository.deleteAllSubjectEducatorLinksByEducatorId(id);
        educatorRepository.deleteAllAccountEducatorLinksByEducatorId(id);
        deleteEducatorUseCase.deleteById(id);
    }
}
