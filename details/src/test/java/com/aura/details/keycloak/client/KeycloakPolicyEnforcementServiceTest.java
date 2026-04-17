package com.aura.details.keycloak.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakPolicyEnforcementServiceTest {

    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private RestClient.RequestBodySpec requestBodySpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    private KeycloakPolicyEnforcementService service;

    @BeforeEach
    void setUp() {
        service = new KeycloakPolicyEnforcementService(restClient, "aura-application-client");
    }

    @Test
    void isAuthorized_whenKeycloakGrantsPermission_returnsTrue() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn("{\"result\":true}");

        boolean result = service.isAuthorized("user-access-token", "outlet", "create");

        assertThat(result).isTrue();
    }

    @Test
    void isAuthorized_whenKeycloakDeniesPermission_returnsFalse() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenThrow(
                new org.springframework.web.client.HttpClientErrorException(
                        org.springframework.http.HttpStatus.FORBIDDEN));

        boolean result = service.isAuthorized("user-access-token", "outlet", "create");

        assertThat(result).isFalse();
    }
}
