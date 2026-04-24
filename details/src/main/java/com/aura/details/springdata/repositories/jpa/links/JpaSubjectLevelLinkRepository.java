package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectLevelLinkRepository extends JpaRepository<SubjectLevelLinkModel, UUID>, JpaSpecificationExecutor<SubjectLevelLinkModel> {
}

