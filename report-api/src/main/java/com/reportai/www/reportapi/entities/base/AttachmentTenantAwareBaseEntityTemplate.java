package com.reportai.www.reportapi.entities.base;

import com.reportai.www.reportapi.entities.helpers.Attachment;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@ToString
@NoArgsConstructor
public abstract class AttachmentTenantAwareBaseEntityTemplate<E, F, A extends AttachmentTenantAwareBaseEntityTemplate<E, F, A>> extends TenantAwareBaseEntity implements Attachment<E, F, A> {

    @PreRemove
    public void preRemove() {
        removeFromRelatedEntities();
    }

}
