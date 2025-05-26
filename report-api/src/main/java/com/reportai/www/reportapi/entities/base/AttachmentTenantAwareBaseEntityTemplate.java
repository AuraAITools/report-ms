package com.reportai.www.reportapi.entities.base;

import com.reportai.www.reportapi.entities.helpers.Attachment;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class AttachmentTenantAwareBaseEntityTemplate<E, F> extends TenantAwareBaseEntity implements Attachment<E, F> {

    @PreRemove
    public void preRemove() {
        removeFromRelatedEntities();
    }

}
