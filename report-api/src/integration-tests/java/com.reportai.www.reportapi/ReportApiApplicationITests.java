package com.reportai.www.reportapi;

import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("integration-test")
public class ReportApiApplicationITests {

    @MockitoBean
    private RealmResource realmResource;

    @MockitoBean
    private ClientResource clientResource;

    @MockitoBean
    private UsersResource usersResource;

    @Test
    void contextLoads() {
    }
}
