package com.reportai.www.reportapi.configproperties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConfigurationProperties(prefix = "oauth2.jwt")
@Getter
@Setter
@ConditionalOnProperty(name = "oauth2.security.enabled", havingValue = "true")
public class OauthResourceServerConfigProperties {

    private String secret;

    @PostConstruct
    private void postValidation() {
        if (this.secret.isEmpty()) {
            throw new RuntimeException("JWT SECRET is missing");
        }
    }

}
