package com.aura.details.spicedb;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.authzed.api.v1.CheckPermissionRequest;
import com.authzed.api.v1.CheckPermissionResponse;
import com.authzed.api.v1.Consistency;
import com.authzed.api.v1.DeleteRelationshipsRequest;
import com.authzed.api.v1.LookupPermissionship;
import com.authzed.api.v1.LookupResourcesRequest;
import com.authzed.api.v1.LookupResourcesResponse;
import com.authzed.api.v1.ObjectReference;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.ReadRelationshipsRequest;
import com.authzed.api.v1.ReadRelationshipsResponse;
import com.authzed.api.v1.RelationshipFilter;
import com.authzed.api.v1.RelationshipUpdate;
import com.authzed.api.v1.SubjectFilter;
import com.authzed.api.v1.SubjectReference;
import com.authzed.api.v1.WriteRelationshipsRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    public void deleteAllRelationships(String resourceType) {
        log.debug("Deleting all relationships for resource type: {}", resourceType);

        permissionsService.deleteRelationships(DeleteRelationshipsRequest.newBuilder()
                .setRelationshipFilter(RelationshipFilter.newBuilder()
                        .setResourceType(resourceType)
                        .build())
                .build());
    }

    @Override
    public void deleteAllRelationshipsForResource(String resourceType, String resourceId) {
        log.debug("Deleting all relationships for resource: {}:{}", resourceType, resourceId);

        permissionsService.deleteRelationships(DeleteRelationshipsRequest.newBuilder()
                .setRelationshipFilter(RelationshipFilter.newBuilder()
                        .setResourceType(resourceType)
                        .setOptionalResourceId(resourceId)
                        .build())
                .build());
    }

    @Override
    public void writeRelationships(List<Relationship> relationships) {
        log.debug("Writing {} relationships, relationships=[{}] in batch", relationships.size(), Arrays.toString(relationships.toArray()));

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
        log.info("Checking permission: {}:{}#{}@{}:{}", resourceType, resourceId, permission, subjectType, subjectId);

        var request = CheckPermissionRequest.newBuilder()
                .setConsistency(Consistency.newBuilder().setFullyConsistent(true).build())
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

    @Override
    public List<String> lookupResources(String resourceType, String permission,
                                        String subjectType, String subjectId) {
        log.debug("Looking up resources: type={}, permission={}, subject={}:{}",
                resourceType, permission, subjectType, subjectId);

        var request = LookupResourcesRequest.newBuilder()
                .setConsistency(Consistency.newBuilder().setFullyConsistent(true).build())
                .setResourceObjectType(resourceType)
                .setPermission(permission)
                .setSubject(SubjectReference.newBuilder()
                        .setObject(ObjectReference.newBuilder()
                                .setObjectType(subjectType)
                                .setObjectId(subjectId)
                                .build())
                        .build())
                .build();

        List<String> resourceIds = new ArrayList<>();
        Iterator<LookupResourcesResponse> responses = permissionsService.lookupResources(request);
        responses.forEachRemaining(response -> {
            if (response.getPermissionship() == LookupPermissionship.LOOKUP_PERMISSIONSHIP_HAS_PERMISSION) {
                resourceIds.add(response.getResourceObjectId());
            }
        });
        return resourceIds;
    }

    @Override
    public List<RelationshipTuple> readRelationships(String resourceType,
                                                     String resourceId,
                                                     String relation,
                                                     String subjectType,
                                                     String subjectId) {
        log.debug("Reading relationships: type={}, resourceId={}, relation={}, subject={}:{}",
                resourceType, resourceId, relation, subjectType, subjectId);

        var subjectFilter = SubjectFilter.newBuilder().setSubjectType(subjectType);
        if (subjectId != null) {
            subjectFilter.setOptionalSubjectId(subjectId);
        }

        var filterBuilder = RelationshipFilter.newBuilder()
                .setResourceType(resourceType)
                .setOptionalSubjectFilter(subjectFilter.build());
        if (resourceId != null) {
            filterBuilder.setOptionalResourceId(resourceId);
        }
        if (relation != null) {
            filterBuilder.setOptionalRelation(relation);
        }

        var request = ReadRelationshipsRequest.newBuilder()
                .setConsistency(Consistency.newBuilder().setFullyConsistent(true).build())
                .setRelationshipFilter(filterBuilder.build())
                .build();

        List<RelationshipTuple> tuples = new ArrayList<>();
        Iterator<ReadRelationshipsResponse> responses = permissionsService.readRelationships(request);
        responses.forEachRemaining(response -> {
            var relationship = response.getRelationship();
            tuples.add(new RelationshipTuple(
                    relationship.getResource().getObjectId(),
                    relationship.getRelation(),
                    relationship.getSubject().getObject().getObjectId()));
        });
        return tuples;
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
