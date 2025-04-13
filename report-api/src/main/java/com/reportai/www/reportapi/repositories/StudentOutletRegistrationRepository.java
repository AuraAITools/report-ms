package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentOutletRegistrationRepository extends JpaRepository<StudentOutletRegistration, UUID>, JpaSpecificationExecutor<StudentOutletRegistration> {
}