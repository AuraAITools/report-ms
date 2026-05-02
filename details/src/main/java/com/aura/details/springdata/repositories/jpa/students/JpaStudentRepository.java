package com.aura.details.springdata.repositories.jpa.students;

import com.aura.details.springdata.models.students.StudentModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentRepository extends JpaRepository<StudentModel, UUID>, JpaSpecificationExecutor<StudentModel> {

    @Query("SELECT s.id, a.userId FROM StudentModel s JOIN s.accountStudentLinks asl JOIN asl.account a WHERE s.id IN :studentIds")
    List<Object[]> findUserIdsByIds(@Param("studentIds") List<UUID> studentIds);
}

