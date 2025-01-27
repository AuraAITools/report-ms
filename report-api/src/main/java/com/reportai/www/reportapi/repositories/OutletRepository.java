package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Outlet;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutletRepository extends JpaRepository<Outlet, UUID> {
}
