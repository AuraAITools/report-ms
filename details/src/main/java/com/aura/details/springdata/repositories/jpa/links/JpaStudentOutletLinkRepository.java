package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.StudentOutletLinkModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentOutletLinkRepository extends JpaRepository<StudentOutletLinkModel, UUID>, JpaSpecificationExecutor<StudentOutletLinkModel> {

    void deleteAllByStudentId(UUID studentId);
}

