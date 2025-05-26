package com.reportai.www.reportapi.api.v1.outlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportai.www.reportapi.api.v1.institutions.BaseIntegrationTest;
import com.reportai.www.reportapi.api.v1.outlets.requests.CreateOutletRequestDTO;
import com.reportai.www.reportapi.api.v1.outlets.requests.UpdateOutletRequestDTO;
import com.reportai.www.reportapi.contexts.requests.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"/sql/institutions.sql", "/sql/outlets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OutletsControllerITest extends BaseIntegrationTest {
    private static final String TENANT_ID = "00000000-0000-0000-0000-000000000000";
    private static final String INSTITUTION_ID = "00000000-0000-0000-0000-000000000000";
    private static final String FIRST_OUTLET_ID = "00000000-0000-0000-0000-000000000002";
    private static final String SECOND_OUTLET_ID = "00000000-0000-0000-0000-000000000003";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(tenantFilter)
                .build();
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("DELETE FROM outlets");
        jdbcTemplate.execute("DELETE FROM institutions");
        TenantContext.clear();
    }

    @Test
    @WithMockUser
    void createOutletForInstitution() throws Exception {
        TenantContext.setTenantId(TENANT_ID);
        CreateOutletRequestDTO request = CreateOutletRequestDTO
                .builder()
                .name("Test Outlet")
                .address("123 Test Street")
                .postalCode("123456")
                .contactNumber("+65 1234 5678")
                .email("test@outlet.com")
                .description("Test description")
                .build();
        when(clientResource.roles()).thenReturn(rolesResource);
        doNothing().when(rolesResource).create(any(RoleRepresentation.class));
        mockMvc.perform(post("/api/v1/institutions/{id}/outlets", INSTITUTION_ID)
                        .with(tenant1InstitutionAdminJwt(outletRepository))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Outlet"))
                .andExpect(jsonPath("$.address").value("123 Test Street"))
                .andExpect(jsonPath("$.postal_code").value("123456"))
                .andExpect(jsonPath("$.contact_number").value("+65 1234 5678"))
                .andExpect(jsonPath("$.email").value("test@outlet.com"))
                .andExpect(jsonPath("$.description").value("Test description"));
    }

    @Test
    @WithMockUser
    void updateOutletForInstitution() throws Exception {
        TenantContext.setTenantId(TENANT_ID);
        UpdateOutletRequestDTO request = UpdateOutletRequestDTO
                .builder()
                .name("Updated Outlet")
                .address("456 Updated Street")
                .postalCode("654321")
                .contactNumber("+65 8765 4321")
                .email("updated@outlet.com")
                .description("Updated description")
                .build();

        mockMvc.perform(patch("/api/v1/institutions/{id}/outlets/{outlet_id}",
                        INSTITUTION_ID, FIRST_OUTLET_ID)
                        .with(tenant1InstitutionAdminJwt(outletRepository))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Outlet"))
                .andExpect(jsonPath("$.postal_code").value("654321"))
                .andExpect(jsonPath("$.address").value("456 Updated Street"))
                .andExpect(jsonPath("$.contact_number").value("+65 8765 4321"))
                .andExpect(jsonPath("$.email").value("updated@outlet.com"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    @WithMockUser
    void getOutletsForInstitution() throws Exception {
        TenantContext.setTenantId(TENANT_ID);
        mockMvc.perform(get("/api/v1/institutions/{id}/outlets", INSTITUTION_ID)
                        .with(tenant1InstitutionAdminJwt(outletRepository)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(FIRST_OUTLET_ID))
                .andExpect(jsonPath("$[0].name").value("First Outlet"))
                .andExpect(jsonPath("$[0].address").value("123 Outlet Street, Singapore"))
                .andExpect(jsonPath("$[0].postal_code").value("123456"))
                .andExpect(jsonPath("$[0].contact_number").value("+65 8888 1111"))
                .andExpect(jsonPath("$[0].description").value("First outlet description"))
                .andExpect(jsonPath("$[0].email").value("first@outlet.com"))
                .andExpect(jsonPath("$[1].id").value(SECOND_OUTLET_ID))
                .andExpect(jsonPath("$[1].name").value("Second Outlet"))
                .andExpect(jsonPath("$[1].address").value("456 Outlet Street, Singapore"))
                .andExpect(jsonPath("$[1].postal_code").value("654321"))
                .andExpect(jsonPath("$[1].contact_number").value("+65 8888 2222"))
                .andExpect(jsonPath("$[1].description").value("Second outlet description"))
                .andExpect(jsonPath("$[1].email").value("second@outlet.com"));
    }

    @Test
    @WithMockUser
    void getExpandedOutletsForInstitution() throws Exception {
        TenantContext.setTenantId(TENANT_ID);
        mockMvc.perform(get("/api/v1/institutions/{id}/outlets/expand", INSTITUTION_ID)
                        .with(tenant1InstitutionAdminJwt(outletRepository)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(FIRST_OUTLET_ID))
                .andExpect(jsonPath("$[0].name").value("First Outlet"))
                .andExpect(jsonPath("$[0].address").value("123 Outlet Street, Singapore"))
                .andExpect(jsonPath("$[0].postal_code").value("123456"))
                .andExpect(jsonPath("$[0].contact_number").value("+65 8888 1111"))
                .andExpect(jsonPath("$[0].description").value("First outlet description"))
                .andExpect(jsonPath("$[0].email").value("first@outlet.com"))
                .andExpect(jsonPath("$[0].students").exists())
                .andExpect(jsonPath("$[0].educators").exists())
                .andExpect(jsonPath("$[0].courses").exists())
                .andExpect(jsonPath("$[1].id").value(SECOND_OUTLET_ID))
                .andExpect(jsonPath("$[1].name").value("Second Outlet"))
                .andExpect(jsonPath("$[1].address").value("456 Outlet Street, Singapore"))
                .andExpect(jsonPath("$[1].postal_code").value("654321"))
                .andExpect(jsonPath("$[1].contact_number").value("+65 8888 2222"))
                .andExpect(jsonPath("$[1].description").value("Second outlet description"))
                .andExpect(jsonPath("$[1].email").value("second@outlet.com"))
                .andExpect(jsonPath("$[1].students").exists())
                .andExpect(jsonPath("$[1].educators").exists())
                .andExpect(jsonPath("$[1].courses").exists());
    }

    // TODO: Implement the test for adding a student to an outlet
//    @Test
//    @WithMockUser
//    void addStudentToOutlet() throws Exception {
//        TenantContext.setTenantId(TENANT_ID);
//        mockMvc.perform(patch("/api/v1/institutions/{id}/outlets/{outlet_id}/students/{student_id}",
//                        INSTITUTION_ID,
//                        FIRST_OUTLET_ID,
//                        "00000000-0000-0000-0000-000000000000")
//                        .with(tenant1InstitutionAdminJwt(outletRepository)))
//                .andExpect(status().isOk());
//    }
}