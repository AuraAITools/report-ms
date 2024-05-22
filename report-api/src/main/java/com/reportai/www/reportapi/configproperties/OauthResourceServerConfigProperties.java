package com.reportai.www.reportapi.configproperties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.isNull;

@Slf4j
@Component
@ConfigurationProperties(prefix = "oauth2.jwt")
@Getter
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
