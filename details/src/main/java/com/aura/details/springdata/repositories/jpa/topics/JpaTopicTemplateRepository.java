package com.aura.details.springdata.repositories.jpa.topics;

import com.aura.details.springdata.models.topics.TopicTemplateModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicTemplateRepository extends JpaRepository<TopicTemplateModel, UUID>, JpaSpecificationExecutor<TopicTemplateModel> {
}

