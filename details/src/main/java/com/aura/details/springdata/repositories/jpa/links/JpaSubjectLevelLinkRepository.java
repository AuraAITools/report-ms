package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectLevelLinkRepository extends JpaRepository<SubjectLevelLinkModel, UUID>, JpaSpecificationExecutor<SubjectLevelLinkModel> {

    void deleteAllBySubjectId(UUID subjectId);

    void deleteAllByLevelId(UUID levelId);

    @Query("SELECT l.level.id FROM SubjectLevelLinkModel l WHERE l.subject.id = :subjectId")
    List<UUID> findLevelIdsBySubjectId(@Param("subjectId") UUID subjectId);

    @Query("SELECT l.subject.id FROM SubjectLevelLinkModel l WHERE l.level.id = :levelId")
    List<UUID> findSubjectIdsByLevelId(@Param("levelId") UUID levelId);
}

