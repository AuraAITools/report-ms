package com.reportai.www.reportapi.entities.helpers;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.Hibernate;

/**
 * Helper utilities to update persistent context for bidrectional relationship mappings
 * Supported:
 * - Many to Many relationships
 * - Many to One relationships
 * - One to One relationships
 */
public class EntityRelationshipUtils {

    /**
     * Updates bidirectional many-to-many relationship without triggering lazy loading
     *
     * @param <T>                Type of the owner collection elements
     * @param <E>                Type of the element collection elements
     * @param owner              The owner object to add to the element's collection
     * @param element            The element to add
     * @param ownerCollection    The collection on the owner side to add the element to
     * @param nonOwnerCollection The collection of owner elements on the non-owner side that should reference back
     * @return true if the owner collection was modified
     */
    public static <T, E> E addToManyToMany(
            E owner, T element, Set<T> ownerCollection,
            Set<E> nonOwnerCollection
    ) {
        ownerCollection.add(element); // add element into owner Collection

        if (Hibernate.isInitialized(nonOwnerCollection)) { // if non-owning side is initialised, update persistence context
            nonOwnerCollection.add(owner);
        }
        return owner;
    }

    /**
     * Updates bidirectional many-to-many relationship for a collection of elements
     */
    public static <T, E> E addAllToManyToMany(
            E owner, Collection<T> elements, Set<T> ownerCollection,
            Function<T, Set<E>> nonOwnerCollectionGetter
    ) {
        ownerCollection.addAll(elements);
        elements.forEach(element -> {
            Set<E> nonOwnerCollection = nonOwnerCollectionGetter.apply(element);

            if (Hibernate.isInitialized(nonOwnerCollection)) {
                nonOwnerCollection.add(owner);
            }
        });
        return owner;
    }


    /**
     * Many to one relationships
     * updates the Many and One side's persistence context
     * only to be used on the owning side  (many side)
     *
     * @param <T>                type of non owning side
     * @param <E>                type of owning side
     * @param owner
     * @param nonOwner
     * @param ownerSetter
     * @param nonOwnerCollection
     * @return
     */
    public static <T, E> E addToManyToOne(E owner, T nonOwner, Consumer<T> ownerSetter, Set<E> nonOwnerCollection) {
        ownerSetter.accept(nonOwner);
        if (Hibernate.isInitialized(nonOwnerCollection)) {
            nonOwnerCollection.add(owner);
        }
        return owner;
    }

    /**
     * One to One relationships
     * updates both entities on the one to one persistence context
     * only to be used on the owning side of one to one
     */
    public static <T, E> E setOneToOne(E owner, T nonOwner, Consumer<T> ownerSideSetter, Consumer<E> nonOwnerSideSetter) {
        ownerSideSetter.accept(nonOwner);
        nonOwnerSideSetter.accept(owner);
        return owner;
    }


}
