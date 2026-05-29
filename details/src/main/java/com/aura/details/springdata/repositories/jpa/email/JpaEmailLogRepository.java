package com.aura.details.springdata.repositories.jpa.email;

import com.aura.core.entities.email.EmailLogStatus;
import com.aura.details.springdata.models.email.EmailLogModel;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEmailLogRepository extends JpaRepository<EmailLogModel, UUID> {

    Optional<EmailLogModel> findFirstByTenantIdAndDedupKeyAndStatusInOrderByCreatedAtDesc(
            String tenantId,
            String dedupKey,
            Collection<EmailLogStatus> statuses
    );
}
