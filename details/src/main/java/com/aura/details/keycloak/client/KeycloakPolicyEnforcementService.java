package com.aura.details.keycloak.client;

import com.aura.core.clients.PolicyEnforcementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

public class KeycloakPolicyEnforcementService implements PolicyEnforcementService {
    private final Logger log = LoggerFactory.getLogger(KeycloakPolicyEnforcementService.class);
    private final RestClient restClient;
    private final String audience;

    public KeycloakPolicyEnforcementService(RestClient restClient, String audience) {
        this.restClient = restClient;
        this.audience = audience;
    }

    @Override
    public boolean isAuthorized(String accessToken, String resource, String scope) {
        String body = "grant_type=urn:ietf:params:oauth:grant-type:uma-ticket"
                + "&audience=" + audience
                + "&permission=" + resource + "#" + scope
                + "&response_mode=decision";
        try {
            restClient.post()
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);
            log.debug("permission granted: {}#{} for token", resource, scope);
            return true;
        } catch (HttpClientErrorException e) {
            log.debug("permission denied: {}#{} — {}", resource, scope, e.getStatusCode());
            return false;
        }
    }
}
