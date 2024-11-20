package com.reportai.www.reportapi.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Outlet extends BaseEntity {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Account> adminAccounts;
}
