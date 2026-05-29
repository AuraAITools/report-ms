package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.PriceRecord;
import com.aura.core.repositories.PriceRecordRepository;
import com.aura.details.springdata.mappers.lessons.PriceRecordMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.PriceRecordModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaPriceRecordRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class PriceRecordRepositoryImpl implements PriceRecordRepository {
    private final JpaPriceRecordRepository jpaPriceRecordRepository;
    private final EntityManager entityManager;

    @Override
    public PriceRecord save(PriceRecord priceRecord) {
        PriceRecordModel model = PriceRecordMapper.INSTANCE.toModelEntity(priceRecord);
        model.setCourse(Refs.ref(entityManager, CourseModel.class, priceRecord.getCourseId()));
        PriceRecordModel newPriceRecord = jpaPriceRecordRepository.save(model);
        return PriceRecordMapper.INSTANCE.toDomainEntity(newPriceRecord);
    }

    @Override
    public @Nullable PriceRecord findById(String id) {
        Optional<PriceRecordModel> priceRecord = jpaPriceRecordRepository.findById(UUID.fromString(id));
        return priceRecord.map(PriceRecordMapper.INSTANCE::toDomainEntity).orElse(null);
    }
}
