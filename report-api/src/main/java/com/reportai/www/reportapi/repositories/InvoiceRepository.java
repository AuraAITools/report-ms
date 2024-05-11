package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}
