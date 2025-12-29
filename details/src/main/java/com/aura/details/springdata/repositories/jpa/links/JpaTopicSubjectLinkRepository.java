package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.tables.links.TopicSubjectLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicSubjectLinkRepository extends JpaRepository<TopicSubjectLinkTable, UUID>, JpaSpecificationExecutor<TopicSubjectLinkTable> {
}

