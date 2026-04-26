package com.aura.details.spicedb;

import com.authzed.api.v1.CheckPermissionRequest;
import com.authzed.api.v1.CheckPermissionResponse;
import com.authzed.api.v1.Consistency;
import com.authzed.api.v1.ObjectReference;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.RelationshipUpdate;
import com.authzed.api.v1.SubjectReference;
import com.authzed.api.v1.WriteRelationshipsRequest;
import com.aura.core.clients.RelationshipAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SpiceDbRelationshipService implements RelationshipAuthorizationService {

    private final PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

    @Override
    public void writeRelationship(String resourceType, String resourceId,
                                  String relation, String subjectType, String subjectId) {
        log.debug("Writing relationship: {}:{}#{}@{}:{}", resourceType, resourceId, relation, subjectType, subjectId);

        var request = WriteRelationshipsRequest.newBuilder()
                .addUpdates(buildRelationshipUpdate(
                        RelationshipUpdate.Operation.OPERATION_TOUCH,
                        resourceType, resourceId, relation, subjectType, subjectId))
                .build();

        permissionsService.writeRelationships(request);
    }

    @Override
    public void deleteRelationship(String resourceType, String resourceId,
                                   String relation, String subjectType, String subjectId) {
        log.debug("Deleting relationship: {}:{}#{}@{}:{}", resourceType, resourceId, relation, subjectType, subjectId);

        var request = WriteRelationshipsRequest.newBuilder()
                .addUpdates(buildRelationshipUpdate(
                        RelationshipUpdate.Operation.OPERATION_DELETE,
                        resourceType, resourceId, relation, subjectType, subjectId))
                .build();

        permissionsService.writeRelationships(request);
    }

    @Override
    public void writeRelationships(List<Relationship> relationships) {
        log.debug("Writing {} relationships in batch", relationships.size());

        var requestBuilder = WriteRelationshipsRequest.newBuilder();

        for (Relationship rel : relationships) {
            requestBuilder.addUpdates(buildRelationshipUpdate(
                    RelationshipUpdate.Operation.OPERATION_TOUCH,
                    rel.resourceType(), rel.resourceId(),
                    rel.relation(), rel.subjectType(), rel.subjectId()));
        }

        permissionsService.writeRelationships(requestBuilder.build());
    }

    @Override
    public boolean checkPermission(String resourceType, String resourceId,
                                   String permission, String subjectType, String subjectId) {
        log.debug("Checking permission: {}:{}#{}@{}:{}", resourceType, resourceId, permission, subjectType, subjectId);

        var request = CheckPermissionRequest.newBuilder()
                .setConsistency(Consistency.newBuilder().setMinimizeLatency(true).build())
                .setResource(ObjectReference.newBuilder()
                        .setObjectType(resourceType)
                        .setObjectId(resourceId)
                        .build())
                .setPermission(permission)
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectType(subjectType)
                                .setObjectId(subjectId)
                                .build())
                        .build())
                .build();

        CheckPermissionResponse response = permissionsService.checkPermission(request);
        return response.getPermissionship() == CheckPermissionResponse.Permissionship.PERMISSIONSHIP_HAS_PERMISSION;
    }

    private RelationshipUpdate buildRelationshipUpdate(RelationshipUpdate.Operation operation,
                                                       String resourceType, String resourceId,
                                                       String relation, String subjectType, String subjectId) {
        return RelationshipUpdate.newBuilder()
                .setOperation(operation)
                .setRelationship(com.authzed.api.v1.Relationship.newBuilder()
                        .setResource(ObjectReference.newBuilder()
                                .setObjectType(resourceType)
                                .setObjectId(resourceId)
                                .build())
                        .setRelation(relation)
                        .setSubject(SubjectReference.newBuilder()
                                .setObject(ObjectReference.newBuilder()
                                        .setObjectType(subjectType)
                                        .setObjectId(subjectId)
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
