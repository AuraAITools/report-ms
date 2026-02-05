package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.tables.subjects.SubjectTable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {
    private final JpaSubjectRepository jpaSubjectRepository;

    @Override
    public Subject save(Subject subject) {
        SubjectTable createdSubject = jpaSubjectRepository.save(SubjectMapper.INSTANCE.toTableEntity(subject));
        return SubjectMapper.INSTANCE.toDomainEntity(createdSubject);
    }
}
