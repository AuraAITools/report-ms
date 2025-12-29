package com.aura.details.springdata.repositories.jpa.topics;

import com.aura.details.springdata.tables.topics.TopicSubjectLinkTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicSubjectLinkTemplateRepository extends JpaRepository<TopicSubjectLinkTemplateTable, UUID>, JpaSpecificationExecutor<TopicSubjectLinkTemplateTable> {
}

