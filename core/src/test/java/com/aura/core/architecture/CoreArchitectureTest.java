package com.aura.core.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Core module architecture")
@AnalyzeClasses(packages = "com.aura.core")
public class CoreArchitectureTest {

    @ArchTest
    public static final ArchRule core_should_not_depend_on_details_or_application =
            noClasses().that().resideInAPackage("com.aura.core..")
                    .should().dependOnClassesThat().resideInAnyPackage("com.aura.details..", "com.aura.app..");
}
