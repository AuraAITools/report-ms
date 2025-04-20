package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.educators.Educator;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
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

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OutletEducatorAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_outlet_educator",
                        columnNames = {"outlet_id", "educator_id"}
                )
        })
public class OutletEducatorAttachment extends AttachmentTenantAwareBaseEntityTemplate<Outlet, Educator, OutletEducatorAttachment> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private Educator educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @Override
    public Outlet getFirstEntity() {
        return outlet;
    }

    @Override
    public Educator getSecondEntity() {
        return educator;
    }

    @Override
    public Collection<OutletEducatorAttachment> getFirstEntityAttachments() {
        return outlet != null ? outlet.getOutletEducatorAttachments() : null;
    }

    @Override
    public Collection<OutletEducatorAttachment> getSecondEntityAttachments() {
        return educator != null ? educator.getOutletEducatorAttachments() : null;
    }

    @Override
    public OutletEducatorAttachment create(Outlet entity1, Educator entity2) {
        return OutletEducatorAttachment
                .builder()
                .outlet(entity1)
                .educator(entity2)
                .build();
    }
}
