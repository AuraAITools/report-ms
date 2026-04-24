package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountStudentLinkRepository extends JpaRepository<AccountStudentLinkModel, UUID>, JpaSpecificationExecutor<AccountStudentLinkModel> {
}

