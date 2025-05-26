package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
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
@Table(name = "AccountStudentAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_student",
                        columnNames = {"account_id", "student_id"}
                )
        })
public class AccountStudentAttachment extends AttachmentTenantAwareBaseEntityTemplate<Account, Student> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private Student student;

    @Override
    public Account getFirstEntity() {
        return account;
    }

    @Override
    public Student getSecondEntity() {
        return student;
    }

    @Override
    public Collection<AccountStudentAttachment> getFirstEntityAttachments() {
        return account != null ? account.getAccountStudentAttachments() : null;
    }

    @Override
    public Collection<AccountStudentAttachment> getSecondEntityAttachments() {
        return student != null ? student.getAccountStudentAttachments() : null;
    }

    @Override
    public AccountStudentAttachment create(Account entity1, Student entity2) {
        return AccountStudentAttachment
                .builder()
                .account(entity1)
                .student(entity2)
                .build();
    }


}
