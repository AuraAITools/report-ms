package com.aura.core.clients;

public interface PolicyEnforcementService {

    boolean isAuthorized(String accessToken, String resource, String scope);
}
