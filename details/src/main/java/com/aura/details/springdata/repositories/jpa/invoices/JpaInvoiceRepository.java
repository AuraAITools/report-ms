package com.aura.details.springdata.repositories.jpa.invoices;

import com.aura.details.springdata.tables.invoices.InvoiceTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInvoiceRepository extends JpaRepository<InvoiceTable, UUID>, JpaSpecificationExecutor<InvoiceTable> {
}

