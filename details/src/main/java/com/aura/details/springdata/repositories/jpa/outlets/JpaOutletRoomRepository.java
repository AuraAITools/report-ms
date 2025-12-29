package com.aura.details.springdata.repositories.jpa.outlets;

import com.aura.details.springdata.tables.outlets.OutletRoomTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOutletRoomRepository extends JpaRepository<OutletRoomTable, UUID>, JpaSpecificationExecutor<OutletRoomTable> {
}

