package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicSubjectLinkRepository extends JpaRepository<TopicSubjectLinkModel, UUID>, JpaSpecificationExecutor<TopicSubjectLinkModel> {

    @Query("SELECT l.subject.id, l.topic FROM TopicSubjectLinkModel l WHERE l.subject.id IN :subjectIds")
    List<Object[]> findTopicsBySubjectIds(@Param("subjectIds") List<UUID> subjectIds);

    void deleteAllBySubjectId(UUID subjectId);

    void deleteAllByTopicId(UUID topicId);
}
