package com.aura.details.springdata.repositories.jpa.topics;

import com.aura.details.springdata.tables.topics.TopicTable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicRepository extends JpaRepository<TopicTable, UUID>, JpaSpecificationExecutor<TopicTable> {
    Optional<TopicTable> findByName(String name);
}

