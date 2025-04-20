package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.OutletRoom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutletRoomRepository extends JpaRepository<OutletRoom, UUID>, JpaSpecificationExecutor<OutletRoom> {
}