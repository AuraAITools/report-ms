package com.aura.details.springdata.repositories.jpa.topics;

import com.aura.details.springdata.models.topics.TopicModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicRepository extends JpaRepository<TopicModel, UUID>, JpaSpecificationExecutor<TopicModel> {
    Optional<TopicModel> findByName(String name);

    List<TopicModel> findByTenantId(String tenantId);
}

