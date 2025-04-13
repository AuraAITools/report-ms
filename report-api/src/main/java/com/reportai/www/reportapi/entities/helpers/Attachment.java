package com.reportai.www.reportapi.entities.helpers;

import java.util.Collection;

/**
 * Interface for entities that represent many-to-many relationships
 * between two entity types.
 *
 * @param <E> Type of the first entity in the relationship
 * @param <F> Type of the second entity in the relationship
 * @param <A> Type of the attachment (implementing class)
 */
public interface Attachment<E, F, A extends Attachment<E, F, A>> {

    /**
     * Get the first entity in the relationship
     */
    E getFirstEntity();

    /**
     * Get the second entity in the relationship
     */
    F getSecondEntity();

    /**
     * Get the collection of attachments from the first entity
     */
    Collection<A> getFirstEntityAttachments();

    /**
     * Get the collection of attachments from the second entity
     */
    Collection<A> getSecondEntityAttachments();

    /**
     * Create a new instance of the attachment
     */
    A create(E entity1, F entity2);

    /**
     * Sync this attachment with the related entities' collections
     */
    default void syncWithRelatedEntities() {
        Collection<A> firstEntityAttachments = getFirstEntityAttachments();
        Collection<A> secondEntityAttachments = getSecondEntityAttachments();

        if (firstEntityAttachments != null) {
            // Safe because we know this is of type A
            @SuppressWarnings("unchecked")
            A self = (A) this;
            firstEntityAttachments.add(self);
        }

        if (secondEntityAttachments != null) {
            // Safe because we know this is of type A
            @SuppressWarnings("unchecked")
            A self = (A) this;
            secondEntityAttachments.add(self);
        }
    }

    /**
     * Remove this attachment from related entities' collections
     */
    default void removeFromRelatedEntities() {
        Collection<A> firstEntityAttachments = getFirstEntityAttachments();
        Collection<A> secondEntityAttachments = getSecondEntityAttachments();

        if (firstEntityAttachments != null) {
            // Safe because we know this is of type A
            @SuppressWarnings("unchecked")
            A self = (A) this;
            firstEntityAttachments.remove(self);
        }

        if (secondEntityAttachments != null) {
            // Safe because we know this is of type A
            @SuppressWarnings("unchecked")
            A self = (A) this;
            secondEntityAttachments.remove(self);
        }
    }

    /**
     * Factory method to create an attachment and sync it with related entities
     */
    static <E, F, A extends Attachment<E, F, A>> A createAndSync(E entity1, F entity2, A attachmentInstance) {
        if (entity1 == null || entity2 == null) {
            throw new IllegalArgumentException("Entities cannot be null");
        }

        A newAttachment = attachmentInstance.create(entity1, entity2);
        newAttachment.syncWithRelatedEntities();
        return newAttachment;
    }

}