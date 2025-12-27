package com.aura.details.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.aura.details")
public class DetailsArchitectureTest {

    @ArchTest
    public static final ArchRule details_should_not_depend_on_application =
            noClasses().that().resideInAPackage("com.aura.details..")
                    .should().dependOnClassesThat().resideInAnyPackage("com.aura.app..");
}
