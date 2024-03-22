package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Invoices")
public class Invoice extends BaseEntitySupport {

    @OneToOne
    private Institution institution;
}
