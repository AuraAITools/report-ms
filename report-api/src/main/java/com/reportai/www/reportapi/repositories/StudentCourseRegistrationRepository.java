package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.StudentCourseRegistration;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseRegistrationRepository extends JpaRepository<StudentCourseRegistration, UUID> {
}