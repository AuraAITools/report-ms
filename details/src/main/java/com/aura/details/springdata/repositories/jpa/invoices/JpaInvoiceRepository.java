package com.aura.details.springdata.repositories.jpa.invoices;

import com.aura.details.springdata.models.invoices.InvoiceModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInvoiceRepository extends JpaRepository<InvoiceModel, UUID>, JpaSpecificationExecutor<InvoiceModel> {
}

