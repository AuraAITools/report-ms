package com.aura.details.keycloak.client;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.AuthorizationResource;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ResourceResource;
import org.keycloak.admin.client.resource.ResourcesResource;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Keycloak resource authorization service")
@ExtendWith(MockitoExtension.class)
class KeycloakResourceAuthorizationServiceTest {

    @Mock
    private ClientResource clientResource;
    @Mock
    private AuthorizationResource authorizationResource;
    @Mock
    private ResourcesResource resourcesResource;
    @Mock
    private ResourceResource resourceResource;

    private KeycloakResourceAuthorizationService service;

    @BeforeEach
    void setUp() {
        service = new KeycloakResourceAuthorizationService(clientResource);
        when(clientResource.authorization()).thenReturn(authorizationResource);
        when(authorizationResource.resources()).thenReturn(resourcesResource);
    }

    @DisplayName("Should create resource with correct name, type, owner and scopes")
    @Test
    void registerResource_createsResourceWithCorrectProperties() {
        String name = "institution:123";
        String type = "urn:aura:institution";
        String ownerId = "owner-uuid";
        Set<String> scopes = Set.of("view", "manage", "delegate");

        service.registerResource(name, type, ownerId, scopes);

        ArgumentCaptor<ResourceRepresentation> captor = ArgumentCaptor.forClass(ResourceRepresentation.class);
        verify(resourcesResource).create(captor.capture());

        ResourceRepresentation captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo(name);
        assertThat(captured.getType()).isEqualTo(type);
        assertThat(captured.getOwner().getId()).isEqualTo(ownerId);
        assertThat(captured.getOwnerManagedAccess()).isTrue();
        assertThat(captured.getScopes())
                .extracting("name")
                .containsExactlyInAnyOrder("view", "manage", "delegate");
    }

    @DisplayName("Should create resource without owner when owner is null")
    @Test
    void registerResource_withNullOwner_createsResourceOwnedByClient() {
        service.registerResource("institution:456", "urn:aura:institution", null, Set.of("view"));

        ArgumentCaptor<ResourceRepresentation> captor = ArgumentCaptor.forClass(ResourceRepresentation.class);
        verify(resourcesResource).create(captor.capture());

        assertThat(captor.getValue().getOwner()).isNull();
    }

    @DisplayName("Should delete resource when it exists")
    @Test
    void deleteResource_whenResourceExists_removesIt() {
        String name = "institution:123";
        String resourceId = "resource-uuid";

        ResourceRepresentation existing = new ResourceRepresentation();
        existing.setId(resourceId);
        existing.setName(name);

        when(resourcesResource.findByName(name)).thenReturn(List.of(existing));
        when(resourcesResource.resource(resourceId)).thenReturn(resourceResource);

        service.deleteResource(name);

        verify(resourcesResource).resource(resourceId);
        verify(resourceResource).remove();
    }

    @DisplayName("Should do nothing when deleting non-existent resource")
    @Test
    void deleteResource_whenResourceDoesNotExist_doesNothing() {
        when(resourcesResource.findByName("institution:nonexistent")).thenReturn(List.of());

        service.deleteResource("institution:nonexistent");

        verify(resourcesResource, never()).resource(any());
    }
}
