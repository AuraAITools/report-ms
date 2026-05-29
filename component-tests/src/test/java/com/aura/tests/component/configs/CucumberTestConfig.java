package com.aura.tests.component.configs;

import com.aura.app.Application;
import com.aura.tests.component.initializers.KeycloakInitializer;
import com.aura.tests.component.initializers.LocalStackInitializer;
import com.aura.tests.component.initializers.MigrationRunner;
import com.aura.tests.component.initializers.PostgresInitializer;
import com.aura.tests.component.initializers.SpiceDbInitializer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig(initializers = {PostgresInitializer.class, KeycloakInitializer.class, SpiceDbInitializer.class, LocalStackInitializer.class, MigrationRunner.class})
@ActiveProfiles("component-test")
@Import({ClientTestConfig.class})
@Suite
@SelectClasspathResource("features")
@CucumberContextConfiguration
@IncludeEngines(value = {"cucumber"})
public class CucumberTestConfig {

}
