package com.aura.app.configs;

import com.aura.app.security.GraphqlBodyCachingFilter;
import com.aura.app.security.IntrospectionRequestMatcher;
import com.aura.app.security.PublicGraphqlMutationRequestMatcher;
import com.aura.app.security.SpiceDbPermissionEvaluator;
import com.aura.core.clients.RelationshipAuthorizationService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "true")
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String LOCAL_PROFILE = "local";

    @Value("${oauth2.security.resource-server.jwk-set-uri}")
    @NotEmpty(message = "jwk url cannot be empty")
    public String jwksUrl;

    private final Environment environment;


    // TODO: add scopes and userinfo into security context
    // TODO: modularise filterchains
    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        boolean isLocal = Arrays.asList(environment.getActiveProfiles()).contains(LOCAL_PROFILE);

        http
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/graphiql"
                    ).permitAll();
                    auth.requestMatchers(new PublicGraphqlMutationRequestMatcher()).permitAll();
                    if (isLocal) {
                        auth.requestMatchers(new IntrospectionRequestMatcher()).permitAll();
                    }
                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .jwkSetUri(jwksUrl)
                        ));

        // GraphqlBodyCachingFilter is required by PublicGraphqlMutationRequestMatcher
        // (and IntrospectionRequestMatcher in local) so the matcher can read the
        // POST body to decide whether the request hits a public mutation.
        http.addFilterBefore(new GraphqlBodyCachingFilter(), BearerTokenAuthenticationFilter.class);
        if (isLocal) {
            log.info("local profile active: introspection queries on /graphql permitted without authentication");
        }

        return http.build();
    }

    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler(RelationshipAuthorizationService relationshipService) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new SpiceDbPermissionEvaluator(relationshipService));
        return handler;
    }


    @PostConstruct
    private void log() {
        log.info("jwks url: {}", jwksUrl);
    }

}
