package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.tables.links.LessonObjectiveTopicLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonObjectiveTopicLinkRepository extends JpaRepository<LessonObjectiveTopicLinkTable, UUID>, JpaSpecificationExecutor<LessonObjectiveTopicLinkTable> {
}

