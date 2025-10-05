package com.aura.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
@ComponentScan(basePackages = {"com.aura.app", "com.aura.details.tables"})
@EntityScan(basePackages = "com.aura.details.tables")
@EnableJpaRepositories(basePackages = "com.aura.details.repositories.jpa")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
