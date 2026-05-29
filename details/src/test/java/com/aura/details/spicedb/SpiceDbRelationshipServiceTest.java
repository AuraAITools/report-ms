package com.aura.details.spicedb;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.authzed.api.v1.CheckPermissionRequest;
import com.authzed.api.v1.CheckPermissionResponse;
import com.authzed.api.v1.DeleteRelationshipsRequest;
import com.authzed.api.v1.DeleteRelationshipsResponse;
import com.authzed.api.v1.LookupPermissionship;
import com.authzed.api.v1.LookupResourcesRequest;
import com.authzed.api.v1.LookupResourcesResponse;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.RelationshipUpdate;
import com.authzed.api.v1.WriteRelationshipsRequest;
import com.authzed.api.v1.WriteRelationshipsResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("SpiceDB relationship service")
@ExtendWith(MockitoExtension.class)
class SpiceDbRelationshipServiceTest {

    @Mock
    private PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

    @InjectMocks
    private SpiceDbRelationshipService service;

    @Test
    @DisplayName("Should write a relationship with OPERATION_TOUCH")
    void writeRelationship() {
        when(permissionsService.writeRelationships(any(WriteRelationshipsRequest.class)))
                .thenReturn(WriteRelationshipsResponse.getDefaultInstance());

        service.writeRelationship("institution", "inst-1", "admin", "user", "user-1");

        var captor = ArgumentCaptor.forClass(WriteRelationshipsRequest.class);
        verify(permissionsService).writeRelationships(captor.capture());

        WriteRelationshipsRequest request = captor.getValue();
        assertThat(request.getUpdatesCount()).isEqualTo(1);

        RelationshipUpdate update = request.getUpdates(0);
        assertThat(update.getOperation()).isEqualTo(RelationshipUpdate.Operation.OPERATION_TOUCH);
        assertThat(update.getRelationship().getResource().getObjectType()).isEqualTo("institution");
        assertThat(update.getRelationship().getResource().getObjectId()).isEqualTo("inst-1");
        assertThat(update.getRelationship().getRelation()).isEqualTo("admin");
        assertThat(update.getRelationship().getSubject().getObject().getObjectType()).isEqualTo("user");
        assertThat(update.getRelationship().getSubject().getObject().getObjectId()).isEqualTo("user-1");
    }

    @Test
    @DisplayName("Should delete a relationship with OPERATION_DELETE")
    void deleteRelationship() {
        when(permissionsService.writeRelationships(any(WriteRelationshipsRequest.class)))
                .thenReturn(WriteRelationshipsResponse.getDefaultInstance());

        service.deleteRelationship("outlet", "outlet-1", "member", "user", "user-2");

        var captor = ArgumentCaptor.forClass(WriteRelationshipsRequest.class);
        verify(permissionsService).writeRelationships(captor.capture());

        WriteRelationshipsRequest request = captor.getValue();
        assertThat(request.getUpdatesCount()).isEqualTo(1);

        RelationshipUpdate update = request.getUpdates(0);
        assertThat(update.getOperation()).isEqualTo(RelationshipUpdate.Operation.OPERATION_DELETE);
        assertThat(update.getRelationship().getResource().getObjectType()).isEqualTo("outlet");
        assertThat(update.getRelationship().getResource().getObjectId()).isEqualTo("outlet-1");
        assertThat(update.getRelationship().getRelation()).isEqualTo("member");
        assertThat(update.getRelationship().getSubject().getObject().getObjectType()).isEqualTo("user");
        assertThat(update.getRelationship().getSubject().getObject().getObjectId()).isEqualTo("user-2");
    }

    @Test
    @DisplayName("Should write multiple relationships in a single batch request")
    void writeRelationships() {
        when(permissionsService.writeRelationships(any(WriteRelationshipsRequest.class)))
                .thenReturn(WriteRelationshipsResponse.getDefaultInstance());

        var relationships = List.of(
                new RelationshipAuthorizationService.Relationship("institution", "inst-1", "admin", "user", "user-1"),
                new RelationshipAuthorizationService.Relationship("outlet", "outlet-1", "member", "user", "user-2")
        );

        service.writeRelationships(relationships);

        var captor = ArgumentCaptor.forClass(WriteRelationshipsRequest.class);
        verify(permissionsService).writeRelationships(captor.capture());

        WriteRelationshipsRequest request = captor.getValue();
        assertThat(request.getUpdatesCount()).isEqualTo(2);

        RelationshipUpdate first = request.getUpdates(0);
        assertThat(first.getOperation()).isEqualTo(RelationshipUpdate.Operation.OPERATION_TOUCH);
        assertThat(first.getRelationship().getResource().getObjectType()).isEqualTo("institution");
        assertThat(first.getRelationship().getRelation()).isEqualTo("admin");

        RelationshipUpdate second = request.getUpdates(1);
        assertThat(second.getOperation()).isEqualTo(RelationshipUpdate.Operation.OPERATION_TOUCH);
        assertThat(second.getRelationship().getResource().getObjectType()).isEqualTo("outlet");
        assertThat(second.getRelationship().getRelation()).isEqualTo("member");
    }

    @Test
    @DisplayName("Should delete all relationships for a given resource type")
    void deleteAllRelationships() {
        when(permissionsService.deleteRelationships(any(DeleteRelationshipsRequest.class)))
                .thenReturn(DeleteRelationshipsResponse.getDefaultInstance());

        service.deleteAllRelationships("institution");

        var captor = ArgumentCaptor.forClass(DeleteRelationshipsRequest.class);
        verify(permissionsService).deleteRelationships(captor.capture());

        DeleteRelationshipsRequest request = captor.getValue();
        assertThat(request.getRelationshipFilter().getResourceType()).isEqualTo("institution");
    }

    @Test
    @DisplayName("Should delete all relationships scoped to a specific resource id")
    void deleteAllRelationshipsForResource() {
        when(permissionsService.deleteRelationships(any(DeleteRelationshipsRequest.class)))
                .thenReturn(DeleteRelationshipsResponse.getDefaultInstance());

        service.deleteAllRelationshipsForResource("course", "course-1");

        var captor = ArgumentCaptor.forClass(DeleteRelationshipsRequest.class);
        verify(permissionsService).deleteRelationships(captor.capture());

        DeleteRelationshipsRequest request = captor.getValue();
        assertThat(request.getRelationshipFilter().getResourceType()).isEqualTo("course");
        assertThat(request.getRelationshipFilter().getOptionalResourceId()).isEqualTo("course-1");
    }

    @Test
    @DisplayName("Should return true when subject has the requested permission")
    void checkPermissionGranted() {
        var response = CheckPermissionResponse.newBuilder()
                .setPermissionship(CheckPermissionResponse.Permissionship.PERMISSIONSHIP_HAS_PERMISSION)
                .build();
        when(permissionsService.checkPermission(any(CheckPermissionRequest.class)))
                .thenReturn(response);

        boolean result = service.checkPermission("institution", "inst-1", "view", "user", "user-1");

        assertThat(result).isTrue();

        var captor = ArgumentCaptor.forClass(CheckPermissionRequest.class);
        verify(permissionsService).checkPermission(captor.capture());

        CheckPermissionRequest request = captor.getValue();
        assertThat(request.getResource().getObjectType()).isEqualTo("institution");
        assertThat(request.getResource().getObjectId()).isEqualTo("inst-1");
        assertThat(request.getPermission()).isEqualTo("view");
        assertThat(request.getSubject().getObject().getObjectType()).isEqualTo("user");
        assertThat(request.getSubject().getObject().getObjectId()).isEqualTo("user-1");
        assertThat(request.getConsistency().getFullyConsistent()).isTrue();
    }

    @Test
    @DisplayName("Should return false when subject does not have the requested permission")
    void checkPermissionDenied() {
        var response = CheckPermissionResponse.newBuilder()
                .setPermissionship(CheckPermissionResponse.Permissionship.PERMISSIONSHIP_NO_PERMISSION)
                .build();
        when(permissionsService.checkPermission(any(CheckPermissionRequest.class)))
                .thenReturn(response);

        boolean result = service.checkPermission("institution", "inst-1", "manage", "user", "user-99");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return resource IDs from LookupResources streaming response")
    void lookupResources() {
        var response1 = LookupResourcesResponse.newBuilder()
                .setResourceObjectId("outlet-1")
                .setPermissionship(LookupPermissionship.LOOKUP_PERMISSIONSHIP_HAS_PERMISSION)
                .build();
        var response2 = LookupResourcesResponse.newBuilder()
                .setResourceObjectId("outlet-2")
                .setPermissionship(LookupPermissionship.LOOKUP_PERMISSIONSHIP_HAS_PERMISSION)
                .build();

        when(permissionsService.lookupResources(any(LookupResourcesRequest.class)))
                .thenReturn(List.of(response1, response2).iterator());

        List<String> result = service.lookupResources("outlet", "view", "user", "user-1");

        assertThat(result).containsExactly("outlet-1", "outlet-2");

        var captor = ArgumentCaptor.forClass(LookupResourcesRequest.class);
        verify(permissionsService).lookupResources(captor.capture());

        LookupResourcesRequest request = captor.getValue();
        assertThat(request.getResourceObjectType()).isEqualTo("outlet");
        assertThat(request.getPermission()).isEqualTo("view");
        assertThat(request.getSubject().getObject().getObjectType()).isEqualTo("user");
        assertThat(request.getSubject().getObject().getObjectId()).isEqualTo("user-1");
    }

    @Test
    @DisplayName("Should return empty list when no resources match")
    void lookupResourcesEmpty() {
        when(permissionsService.lookupResources(any(LookupResourcesRequest.class)))
                .thenReturn(List.<LookupResourcesResponse>of().iterator());

        List<String> result = service.lookupResources("course", "manage", "user", "user-99");

        assertThat(result).isEmpty();
    }
}
