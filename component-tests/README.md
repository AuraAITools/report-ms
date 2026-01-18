# Component Tests

Prerequisites:

1. have docker installed for test containers
2. have java installed to run DB Runner

## Rationale

Uses Business Driven Development (BDD) with `Cucumber` framework and `Gherkin` syntax to define business requirements
and create tests. Feature files must be **easily understood by non-technical business analysts**, this will ensure that
the
technical delivery matches the business expectations.

## Conventions

1. Feature
    2. A feature should define a user flow
    3. The same set of features should be availble on all platforms: Mobile, Browser & API. The scenarios however can
       differ
3. Scenario
    4. A set of Scenarios make up a feature
5. Tagging
    6. Test type: Label each Scenario with a test type: i.e. `@ComponentTest`
    7. Platform: Label each Scenario with a delivery medium `@Api`, `@Mobile`, `@Browser`

## Reporting

1. Extent reports can be found in `target/extent-reports/ExtentReport-{datetime}.Spark.html`
2. cucumber reports: json & html are found in `target/cucumber.html` & `target/cucumber.json`

## Future

1. Extend component tests to also include screenshots with selenium on Browser/Mobile
2. Abstract to testcontainer
    3. DBRunner