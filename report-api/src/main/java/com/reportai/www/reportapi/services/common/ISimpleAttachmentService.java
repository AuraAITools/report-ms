package com.reportai.www.reportapi.services.common;

import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple implementation for creating a attachment
 * an attachment is defined as a record table of a many-to-many relationship
 *
 * @param <E> entity 1
 * @param <F> entity 2
 */
public interface ISimpleAttachmentService<E, F> {

    /**
     * Creates an attachment
     *
     * @param entity1Id
     * @param entity2Id
     * @param newAttachmentInstance
     * @return
     */
    @Transactional
    default <A extends Attachment<E, F>> A attach(UUID entity1Id, UUID entity2Id, A newAttachmentInstance) {
        E entity1 = getEntity1ReadService().findById(entity1Id);
        F entity2 = getEntity2ReadService().findById(entity2Id);
        A attachment = Attachment.createAndSync(entity1, entity2, newAttachmentInstance);
        return getAttachmentRepository().save(attachment);
    }

    @Transactional
    default void detach(UUID attachmentId) {
        getAttachmentRepository().findById(attachmentId)
                .ifPresentOrElse(
                        attachment -> getAttachmentRepository().delete(attachment),
                        () -> {
                            throw new ResourceNotFoundException(String.format("resource %s with id %s not found", getClass().getName(), attachmentId));
                        }
                );
    }

    ISimpleRead<E> getEntity1ReadService();

    ISimpleRead<F> getEntity2ReadService();

    <A extends Attachment<E, F>> JpaRepository<A, UUID> getAttachmentRepository();

}
