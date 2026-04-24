package com.aura.details.springdata.repositories.jpa.outlets;

import com.aura.details.springdata.models.outlets.OutletRoomModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOutletRoomRepository extends JpaRepository<OutletRoomModel, UUID>, JpaSpecificationExecutor<OutletRoomModel> {
}

