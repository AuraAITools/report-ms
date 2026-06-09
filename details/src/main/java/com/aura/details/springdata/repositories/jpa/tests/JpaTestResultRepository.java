package com.aura.details.springdata.repositories.jpa.tests;

import com.aura.details.springdata.models.tests.TestResultModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTestResultRepository extends JpaRepository<TestResultModel, UUID>, JpaSpecificationExecutor<TestResultModel> {

    @Query("SELECT t.id FROM TestResultModel t WHERE t.subject.id = :subjectId")
    List<UUID> findIdsBySubjectId(@Param("subjectId") UUID subjectId);

    @Modifying
    @Query("UPDATE TestResultModel t SET t.subject = NULL WHERE t.subject.id = :subjectId")
    int nullOutSubjectBySubjectId(@Param("subjectId") UUID subjectId);
}
