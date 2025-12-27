package com.aura.details.repositories.jpa.links;

import com.aura.details.tables.links.TopicSubjectLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicSubjectLinkRepository extends JpaRepository<TopicSubjectLinkTable, UUID>, JpaSpecificationExecutor<TopicSubjectLinkTable> {
}

