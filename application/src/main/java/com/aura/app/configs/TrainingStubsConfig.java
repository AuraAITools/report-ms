package com.aura.app.configs;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// Stub beans active only during the AOT-cache training run. Lets the Spring context
// refresh without hitting Keycloak so the JDK can record class loading/linking.
@Configuration
@Profile("training")
public class TrainingStubsConfig {

    @Bean
    IdentityService stubIdentityService() {
        return new IdentityService() {
            @Override
            public List<UserIdentity> findByEmail(String email) {
                return Collections.emptyList();
            }

            @Override
            public UserIdentity createUserIdentityAndTagToTenant(UserIdentity userIdentity, String tenantId) {
                return userIdentity;
            }

            @Override
            public void tagTenantToUser(String userId, String tenantId) {}

            @Override
            public void untagTenantFromUser(String userId, String tenantId) {}

            @Override
            public void deleteUser(String userId) {}

            @Override
            public void assignRolesToUser(String userId, String... roles) {}

            @Override
            public void sendActionsEmail(String userId, List<String> actions, @Nullable String redirectUri) {}

            @Override
            public boolean hasCompletedRequiredActions(String userId, List<String> requiredActions) {
                return false;
            }
        };
    }
}
