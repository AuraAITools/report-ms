package com.reportai.www.reportapi.clients.keycloak;

public interface AuthClient<U> {
    String createUserAccount(U userDetails);

    void sendPendingActionsToUserEmail(String userId);

}
