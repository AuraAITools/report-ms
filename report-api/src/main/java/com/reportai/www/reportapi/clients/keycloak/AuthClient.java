package com.reportai.www.reportapi.clients.keycloak;

public interface AuthClient<U> {
    String createDefaultUserAccount(U userDetails);

    void sendPendingActionsToUserEmail(String userId);

}
