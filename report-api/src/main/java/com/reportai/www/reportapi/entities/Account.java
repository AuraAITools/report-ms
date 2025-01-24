package com.reportai.www.reportapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * An account is a keycloak account
 * an account can belong to multiple institutions
 * The same account created for a user can be for a client, institution or educator
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Accounts")
public class Account extends BaseEntity {

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String contact;

    // students managed under this account
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Student> students;

    // institutions managed under this account
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Institution> institutions;

    // educators managed under this account
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Educator> educators;

    @ManyToMany(mappedBy = "adminAccounts", fetch = FetchType.LAZY)
    private List<Outlet> outlets;

    @Column(nullable = false)
    private String tenantId;
}
