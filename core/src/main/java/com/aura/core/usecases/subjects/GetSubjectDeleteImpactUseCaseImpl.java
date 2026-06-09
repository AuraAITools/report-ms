package com.aura.core.usecases.subjects;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetSubjectDeleteImpactUseCaseImpl implements GetSubjectDeleteImpactUseCase {
    private final SubjectRepository subjectRepository;

    @Override
    public DeleteImpact getDeleteImpact(String subjectId) {
        return subjectRepository.findDeleteImpact(subjectId);
    }
}
