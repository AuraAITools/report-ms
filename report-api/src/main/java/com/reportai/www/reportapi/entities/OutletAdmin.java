package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "OutletAdmins")
public class OutletAdmin extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @ManyToMany(mappedBy = "outletAdmins", fetch = FetchType.LAZY)
    public List<Outlet> outlets;

    @Column(nullable = false)
    public String tenantId;
}
