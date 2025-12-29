package com.aura.details.springdata.tables.links;

import com.aura.details.springdata.tables.accounts.AccountTable;
import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.students.StudentTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;


@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AccountStudentLinks", uniqueConstraints = {@UniqueConstraint(name = "uk_account_student", columnNames = {"account_id", "student_id"})})
public class AccountStudentLinkTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "account_id")
    private AccountTable account;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private StudentTable student;


}
