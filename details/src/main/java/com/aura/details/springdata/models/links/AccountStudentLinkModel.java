package com.aura.details.springdata.models.links;

import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.students.StudentModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "AccountStudentLinks", uniqueConstraints = {@UniqueConstraint(name = "uk_account_student", columnNames = {"account_id", "student_id"})})
public class AccountStudentLinkModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "account_id")
    private AccountModel account;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private StudentModel student;


}
