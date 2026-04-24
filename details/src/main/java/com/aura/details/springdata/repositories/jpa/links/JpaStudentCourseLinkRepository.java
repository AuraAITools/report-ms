package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentCourseLinkRepository extends JpaRepository<StudentCourseLinkModel, UUID>, JpaSpecificationExecutor<StudentCourseLinkModel> {
}

