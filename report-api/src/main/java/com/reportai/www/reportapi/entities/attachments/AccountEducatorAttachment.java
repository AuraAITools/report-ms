package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.educators.Educator;
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
@Table(name = "AccountEducatorAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_educator",
                        columnNames = {"account_id", "educator_id"}
                )
        })
public class AccountEducatorAttachment extends AttachmentTenantAwareBaseEntityTemplate<Account, Educator> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private Educator educator;

    @Override
    public Account getFirstEntity() {
        return account;
    }

    @Override
    public Educator getSecondEntity() {
        return educator;
    }

    @Override
    public Collection<AccountEducatorAttachment> getFirstEntityAttachments() {
        return account != null ? account.getAccountEducatorAttachments() : null;
    }

    @Override
    public Collection<AccountEducatorAttachment> getSecondEntityAttachments() {
        return educator != null ? educator.getAccountEducatorAttachments() : null;
    }

    @Override
    public AccountEducatorAttachment create(Account entity1, Educator entity2) {
        return AccountEducatorAttachment
                .builder()
                .account(entity1)
                .educator(entity2)
                .build();
    }


}
