package com.reportai.www.reportapi;

import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
public class ReportApiApplicationITests {

    @MockitoBean
    protected RealmResource realmResource;

    @MockitoBean
    protected ClientResource clientResource;

    @MockitoBean
    protected UsersResource usersResource;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientRepresentation clientRepresentation;

    @Test
    void contextLoads() {
    }
}
