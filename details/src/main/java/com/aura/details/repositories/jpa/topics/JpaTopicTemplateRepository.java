package com.aura.details.repositories.jpa.topics;

import com.aura.details.tables.topics.TopicTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicTemplateRepository extends JpaRepository<TopicTemplateTable, UUID>, JpaSpecificationExecutor<TopicTemplateTable> {
}

