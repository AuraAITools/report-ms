package com.aura.app.interceptors;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakAuthorizationInterceptorTest {

    @Test
    void resolveVariable_flatKey_returnsValue() {
        Map<String, Object> variables = Map.of("outletId", "uuid-123");

        String result = KeycloakAuthorizationInterceptor.resolveVariable(variables, "outletId");

        assertThat(result).isEqualTo("uuid-123");
    }

    @Test
    void resolveVariable_nestedKey_returnsValue() {
        Map<String, Object> variables = Map.of(
                "input", Map.of("outletId", "uuid-456")
        );

        String result = KeycloakAuthorizationInterceptor.resolveVariable(variables, "input.outletId");

        assertThat(result).isEqualTo("uuid-456");
    }

    @Test
    void resolveVariable_deeplyNested_returnsValue() {
        Map<String, Object> variables = Map.of(
                "input", Map.of("outlet", Map.of("id", "uuid-789"))
        );

        String result = KeycloakAuthorizationInterceptor.resolveVariable(variables, "input.outlet.id");

        assertThat(result).isEqualTo("uuid-789");
    }

    @Test
    void resolveVariable_missingKey_returnsNull() {
        Map<String, Object> variables = Map.of("other", "value");

        String result = KeycloakAuthorizationInterceptor.resolveVariable(variables, "outletId");

        assertThat(result).isNull();
    }

    @Test
    void resolveVariable_missingNestedKey_returnsNull() {
        Map<String, Object> variables = Map.of("input", Map.of("name", "test"));

        String result = KeycloakAuthorizationInterceptor.resolveVariable(variables, "input.outletId");

        assertThat(result).isNull();
    }

    @Test
    void resolveVariable_nonMapIntermediate_returnsNull() {
        Map<String, Object> variables = Map.of("input", "not-a-map");

        String result = KeycloakAuthorizationInterceptor.resolveVariable(variables, "input.outletId");

        assertThat(result).isNull();
    }

    @Test
    void hasPermission_typeLevelMatch_returnsTrue() {
        Map<String, Object> authorization = Map.of(
                "permissions", List.of(
                        Map.of("rsname", "outlet", "scopes", List.of("create"))
                )
        );
        boolean result = KeycloakAuthorizationInterceptor.hasPermission(authorization, "outlet", "create");
        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_instanceLevelMatch_returnsTrue() {
        Map<String, Object> authorization = Map.of(
                "permissions", List.of(
                        Map.of("rsname", "outlet:outlet-001", "scopes", List.of("view", "manage")),
                        Map.of("rsname", "institution:inst-001", "scopes", List.of("view", "manage"))
                )
        );
        boolean result = KeycloakAuthorizationInterceptor.hasPermission(authorization, "outlet:outlet-001", "view");
        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_noMatchingResource_returnsFalse() {
        Map<String, Object> authorization = Map.of(
                "permissions", List.of(
                        Map.of("rsname", "outlet:outlet-001", "scopes", List.of("view"))
                )
        );
        boolean result = KeycloakAuthorizationInterceptor.hasPermission(authorization, "outlet:outlet-999", "view");
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_matchingResourceWrongScope_returnsFalse() {
        Map<String, Object> authorization = Map.of(
                "permissions", List.of(
                        Map.of("rsname", "outlet", "scopes", List.of("view"))
                )
        );
        boolean result = KeycloakAuthorizationInterceptor.hasPermission(authorization, "outlet", "create");
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_emptyPermissions_returnsFalse() {
        Map<String, Object> authorization = Map.of(
                "permissions", List.of()
        );
        boolean result = KeycloakAuthorizationInterceptor.hasPermission(authorization, "outlet", "create");
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_nullAuthorization_returnsFalse() {
        boolean result = KeycloakAuthorizationInterceptor.hasPermission(null, "outlet", "create");
        assertThat(result).isFalse();
    }

    @Test
    void extractOperationNames_singleQuery_returnsSingleName() {
        String document = "query { getOutletsByPage(institutionId: \"inst-1\") { edges { node { id } } } }";

        List<String> result = KeycloakAuthorizationInterceptor.extractOperationNames(document);

        assertThat(result).containsExactly("getOutletsByPage");
    }

    @Test
    void extractOperationNames_multipleQueries_returnsAllNames() {
        String document = """
                query {
                  getOutletsByPage(institutionId: "inst-1") { edges { node { id } } }
                  getInstitution(institutionId: "inst-1") { id name }
                }
                """;

        List<String> result = KeycloakAuthorizationInterceptor.extractOperationNames(document);

        assertThat(result).containsExactly("getOutletsByPage", "getInstitution");
    }

    @Test
    void extractOperationNames_namedQuery_returnsFieldNames() {
        String document = """
                query MyQuery {
                  getOutletsByPage(institutionId: "inst-1") { edges { node { id } } }
                  getInstitution(institutionId: "inst-1") { id name }
                }
                """;

        List<String> result = KeycloakAuthorizationInterceptor.extractOperationNames(document);

        assertThat(result).containsExactly("getOutletsByPage", "getInstitution");
    }

    @Test
    void extractOperationNames_mutation_returnsMutationName() {
        String document = "mutation { createOutlet(input: {name: \"test\"}) { id name } }";

        List<String> result = KeycloakAuthorizationInterceptor.extractOperationNames(document);

        assertThat(result).containsExactly("createOutlet");
    }

    @Test
    void extractOperationNames_emptyDocument_returnsEmptyList() {
        String document = "";

        List<String> result = KeycloakAuthorizationInterceptor.extractOperationNames(document);

        assertThat(result).isEmpty();
    }

    @Test
    void extractOperationNames_queryWithFieldSubselection_doesNotIncludeNestedFields() {
        String document = """
                query {
                  getOutletsByPage(institutionId: "inst-1") {
                    edges {
                      node {
                        id
                        name
                      }
                    }
                  }
                }
                """;

        List<String> result = KeycloakAuthorizationInterceptor.extractOperationNames(document);

        assertThat(result).containsExactly("getOutletsByPage");
    }
}
