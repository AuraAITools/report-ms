package com.reportai.www.reportapi.api.v1.institutions;

import com.reportai.www.reportapi.contexts.requests.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/institutions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class InstitutionsControllerITest extends BaseIntegrationTest {

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(tenantFilter)
                .build();
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("DELETE FROM institutions");
        jdbcTemplate.execute("DELETE FROM outlets");
    }

    @Test
    @WithMockUser
    void createInstitution() throws Exception {
        String createInstitutionRequest = """
                {
                    "name": "test tuition institution",
                    "email": "test@gmail.com",
                    "contact_number": "96228693",
                    "address": "Yishun Ave 4",
                    "uen": "201713693K"
                }
                """;
        RolesResource rolesResource = mock(RolesResource.class);
        when(clientResource.roles()).thenReturn(rolesResource);
        doNothing().when(rolesResource).create(any(RoleRepresentation.class));
        mockMvc.perform(post("/api/v1/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInstitutionRequest)
                        .with(auraAdminJwt(outletRepository)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("test tuition institution"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.contact_number").value("96228693"))
                .andExpect(jsonPath("$.uen").value("201713693K"));
    }

    @Test
    @WithMockUser
    void updateInstitutionById() throws Exception {

        String updateInstitutionRequest = """
                        {
                                "name": "updated",
                                "email": "newemail@gmail.com",
                                "uen": "1124151258",
                                "address": "new address",
                                "contact_number": "96872341"
                        }
                """;
        TenantContext.setTenantId("00000000-0000-0000-0000-000000000000");
        mockMvc.perform(patch("/api/v1/institutions/{id}", "00000000-0000-0000-0000-000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateInstitutionRequest)
                        .with(tenant1InstitutionAdminJwt(outletRepository)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    @WithMockUser
    void getInstitution() throws Exception {
        TenantContext.setTenantId("00000000-0000-0000-0000-000000000000");
        mockMvc.perform(get("/api/v1/institutions/{id}", "00000000-0000-0000-0000-000000000000")
                        .with(tenant1InstitutionAdminJwt(outletRepository)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("First Institution"))
                .andExpect(jsonPath("$.email").value("first@institution.com"))
                .andExpect(jsonPath("$.uen").value("UEN12345A"))
                .andExpect(jsonPath("$.address").value("123 First Street, Singapore"))
                .andExpect(jsonPath("$.contact_number").value("+65 1234 5678"));
    }

}