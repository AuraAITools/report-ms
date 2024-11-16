package com.reportai.www.reportapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// TODO: specify a private license
@OpenAPIDefinition(
        info = @Info(
                title = "Aura Report Service APIs",
                description = "A set of APIS for the Aura report service",
                version = "v1",
                contact = @Contact(
                        name = "Aura",
                        email = "kevinliudevelopes@gmail.com"
                )
        )
)
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class ReportApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApiApplication.class, args);
    }

}
