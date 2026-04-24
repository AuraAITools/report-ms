package com.aura.details.springdata.mappers.invoices;

import com.aura.core.entities.invoices.Invoice;
import com.aura.details.springdata.models.invoices.InvoiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    Invoice toDomainEntity(InvoiceModel invoiceModel);


    InvoiceModel toModelEntity(Invoice invoice);
}

