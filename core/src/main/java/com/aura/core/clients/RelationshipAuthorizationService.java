package com.aura.core.clients;

import java.util.List;
import org.jspecify.annotations.Nullable;

public interface RelationshipAuthorizationService {
    void writeRelationship(String resourceType, String resourceId,
                           String relation, String subjectType, String subjectId);

    void deleteRelationship(String resourceType, String resourceId,
                            String relation, String subjectType, String subjectId);

    void writeRelationships(List<Relationship> relationships);

    void deleteAllRelationships(String resourceType);

    void deleteAllRelationshipsForResource(String resourceType, String resourceId);

    boolean checkPermission(String resourceType, String resourceId,
                            String permission, String subjectType, String subjectId);

    List<String> lookupResources(String resourceType, String permission,
                                 String subjectType, String subjectId);

    List<RelationshipTuple> readRelationships(String resourceType,
                                              @Nullable String resourceId,
                                              @Nullable String relation,
                                              String subjectType,
                                              @Nullable String subjectId);

    record Relationship(String resourceType, String resourceId,
                        String relation, String subjectType, String subjectId) {
    }

    record RelationshipTuple(String resourceId, String relation, String subjectId) {
    }
}
