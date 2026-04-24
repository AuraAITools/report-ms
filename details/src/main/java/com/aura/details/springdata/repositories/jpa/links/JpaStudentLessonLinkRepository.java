package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentLessonLinkRepository extends JpaRepository<StudentLessonLinkModel, UUID>, JpaSpecificationExecutor<StudentLessonLinkModel> {
}

