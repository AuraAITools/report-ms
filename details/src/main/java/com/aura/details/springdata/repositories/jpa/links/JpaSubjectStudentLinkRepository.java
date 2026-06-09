package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectStudentLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectStudentLinkRepository extends JpaRepository<SubjectStudentLinkModel, UUID>, JpaSpecificationExecutor<SubjectStudentLinkModel> {

    void deleteAllByStudentId(UUID studentId);

    void deleteAllBySubjectId(UUID subjectId);

    @Query("SELECT l.student.id FROM SubjectStudentLinkModel l WHERE l.subject.id = :subjectId")
    List<UUID> findStudentIdsBySubjectId(@Param("subjectId") UUID subjectId);
}

