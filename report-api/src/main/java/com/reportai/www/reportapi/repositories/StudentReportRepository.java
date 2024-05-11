package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentReportRepository extends JpaRepository<StudentReport, UUID> {
}
