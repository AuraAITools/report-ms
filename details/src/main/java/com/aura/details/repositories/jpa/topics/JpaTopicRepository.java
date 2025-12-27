package com.aura.details.repositories.jpa.topics;

import com.aura.details.tables.topics.TopicTable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicRepository extends JpaRepository<TopicTable, UUID>, JpaSpecificationExecutor<TopicTable> {
    Optional<TopicTable> findByName(String name);
}

