package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.PriceRecord;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PriceRecordRepository extends JpaRepository<PriceRecord, UUID>, JpaSpecificationExecutor<PriceRecord> {
}
