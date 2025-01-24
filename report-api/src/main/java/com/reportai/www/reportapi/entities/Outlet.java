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

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Outlets")
public class Outlet extends BaseEntity {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Account> adminAccounts;

    @Column(nullable = false)
    private String tenantId;
}
