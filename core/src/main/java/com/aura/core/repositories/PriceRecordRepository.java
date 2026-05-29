package com.aura.core.repositories;

import com.aura.core.entities.lessons.PriceRecord;
import org.jspecify.annotations.Nullable;

public interface PriceRecordRepository {
    PriceRecord save(PriceRecord priceRecord);

    @Nullable PriceRecord findById(String id);
}
