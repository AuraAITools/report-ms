package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.configproperties.OauthResourceServerConfigProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@Slf4j
@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "true")
public class OAuth2ResourceServerSecurityConfiguration {

    private final OauthResourceServerConfigProperties oauthResourceServerConfigProperties;

    public OAuth2ResourceServerSecurityConfiguration(OauthResourceServerConfigProperties oauthResourceServerConfigProperties) {
        this.oauthResourceServerConfigProperties = oauthResourceServerConfigProperties;
    }

    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth)-> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");  // Adjust if necessary
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");  // Adjust based on your token's claim name

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


    /**
     * supabase does not support JWK uri set atm. but it supports symmetric key signing
     * with the HS256 aka Hmac SHA256 algorithm
     * for now we can just decode by this way
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = oauthResourceServerConfigProperties.getSecret();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    }

    // TODO: add scopes and userinfo into security context
    @PostConstruct
    private void log() {
        log.info("Web security initialising");
    }


}
