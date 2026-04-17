package com.aura.app.interceptors;

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
}
