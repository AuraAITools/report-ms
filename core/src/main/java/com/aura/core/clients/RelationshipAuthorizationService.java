package com.aura.core.clients;

import java.util.List;

public interface RelationshipAuthorizationService {
    void writeRelationship(String resourceType, String resourceId,
                           String relation, String subjectType, String subjectId);

    void deleteRelationship(String resourceType, String resourceId,
                            String relation, String subjectType, String subjectId);

    void writeRelationships(List<Relationship> relationships);

    boolean checkPermission(String resourceType, String resourceId,
                            String permission, String subjectType, String subjectId);

    record Relationship(String resourceType, String resourceId,
                        String relation, String subjectType, String subjectId) {}
}
