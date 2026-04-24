package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.PriceRecordModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPriceRecordRepository extends JpaRepository<PriceRecordModel, UUID>, JpaSpecificationExecutor<PriceRecordModel> {
}

