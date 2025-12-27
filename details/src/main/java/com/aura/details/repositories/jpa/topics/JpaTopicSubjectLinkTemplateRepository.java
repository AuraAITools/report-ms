package com.aura.details.repositories.jpa.topics;

import com.aura.details.tables.topics.TopicSubjectLinkTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicSubjectLinkTemplateRepository extends JpaRepository<TopicSubjectLinkTemplateTable, UUID>, JpaSpecificationExecutor<TopicSubjectLinkTemplateTable> {
}

