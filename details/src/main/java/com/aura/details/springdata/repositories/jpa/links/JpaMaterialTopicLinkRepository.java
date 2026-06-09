package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMaterialTopicLinkRepository extends JpaRepository<MaterialTopicLinkModel, UUID>, JpaSpecificationExecutor<MaterialTopicLinkModel> {

    void deleteAllByMaterialId(UUID materialId);

    void deleteAllByTopicId(UUID topicId);

    @Query("SELECT l.material.id, l.topic FROM MaterialTopicLinkModel l WHERE l.material.id IN :materialIds")
    List<Object[]> findTopicsByMaterialIds(@Param("materialIds") List<UUID> materialIds);

    @Query("SELECT l.material.id FROM MaterialTopicLinkModel l WHERE l.topic.id = :topicId")
    List<UUID> findMaterialIdsByTopicId(@Param("topicId") UUID topicId);
}
