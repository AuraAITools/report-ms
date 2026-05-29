package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpServerErrorException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class StudentStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;
    private Map<String, Object> lastGetStudentResponse;
    private List<Map<String, Object>> lastGetStudentsResponse;
    private Map<String, Object> lastUpdateStudentResponse;
    private Boolean lastDeleteStudentResponse;

    @And("aura-admin creates an account with parent and student with details:")
    @SuppressWarnings("unchecked")
    public void auraAdminCreatesAccountWithParentAndStudent(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().getFirst();

        String parentEmail = row.get("parentEmail");
        String parentFirstName = row.get("parentFirstName");
        String parentLastName = row.get("parentLastName");
        int parentContact = Integer.parseInt(row.get("parentContact"));
        String studentName = row.get("studentName");
        String studentEmail = row.get("studentEmail");
        String studentDateOfBirth = row.get("studentDateOfBirth");

        // 1. Create a fresh account for the parent via the current createAccount mutation
        String createAccountQuery = findGraphqlQueryFromFiles("create-account.graphql");
        Map<String, Object> accountInput = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "email", parentEmail,
                "firstName", parentFirstName,
                "lastName", parentLastName,
                "contact", parentContact
        );
        Map<String, Object> accountResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(createAccountQuery)
                .variables(Map.of("input", accountInput))
                .retrieveSync("createAccount")
                .toEntity(Map.class);
        String accountId = (String) accountResponse.get("id");
        assertThat(accountId).isNotNull();
        testContext.setAccountId(accountId);

        // 2. Ensure a level and a school exist (templates truncated by cleanup hooks)
        String levelId = ensureLevelExists();
        String schoolId = ensureSchoolExists();

        // 3. Create parent + student in one call via createParentAndStudentsInAccount
        String query = findGraphqlQueryFromFiles("create-parent-and-students-in-account.graphql");

        Map<String, Object> studentDetails = new HashMap<>();
        studentDetails.put("name", studentName);
        studentDetails.put("email", studentEmail);
        studentDetails.put("dateOfBirth", studentDateOfBirth);
        studentDetails.put("levelId", levelId);
        studentDetails.put("schoolId", schoolId);
        studentDetails.put("courseEnrollments", List.of());

        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "accountId", accountId,
                "relationship", "PARENT",
                "students", List.of(studentDetails)
        );

        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createParentAndStudentsInAccount")
                .toEntity(Map.class);

        assertThat(response).isNotNull();
        List<Map<String, Object>> students = (List<Map<String, Object>>) response.get("students");
        assertThat(students).isNotEmpty();
        String studentId = (String) students.getFirst().get("id");
        assertThat(studentId).isNotNull();
        testContext.setStudentId(studentId);
        log.info("Created student '{}' with id {} under parent account {}", studentName, studentId, accountId);
    }

    @When("user queries the noted student by id")
    @SuppressWarnings("unchecked")
    public void userQueriesNotedStudentById() {
        String query = findGraphqlQueryFromFiles("get-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getStudentId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            lastGetStudentResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getStudentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user queries student by a random id")
    public void userQueriesStudentByRandomId() {
        String query = findGraphqlQueryFromFiles("get-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getStudentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the student response should contain name {string} and email {string}")
    public void theStudentResponseShouldContainNameAndEmail(String name, String email) {
        Map<String, Object> body = lastGetStudentResponse != null ? lastGetStudentResponse : lastUpdateStudentResponse;
        assertThat(body).as("Expected a student response to be present").isNotNull();
        assertThat(body.get("name")).isEqualTo(name);
        assertThat(body.get("email")).isEqualTo(email);
    }

    @Then("the student response should contain a linked level")
    @SuppressWarnings("unchecked")
    public void theStudentResponseShouldContainALinkedLevel() {
        assertThat(lastGetStudentResponse).isNotNull();
        Map<String, Object> level = (Map<String, Object>) lastGetStudentResponse.get("level");
        assertThat(level).as("Expected the student to have a level").isNotNull();
        assertThat(level.get("id")).isNotNull();
    }

    @Then("the student response should contain a linked school")
    @SuppressWarnings("unchecked")
    public void theStudentResponseShouldContainALinkedSchool() {
        assertThat(lastGetStudentResponse).isNotNull();
        Map<String, Object> school = (Map<String, Object>) lastGetStudentResponse.get("school");
        assertThat(school).as("Expected the student to have a school").isNotNull();
        assertThat(school.get("id")).isNotNull();
    }

    @When("user queries all students in the institution")
    @SuppressWarnings("unchecked")
    public void userQueriesAllStudentsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-students-in-institution.graphql");
        try {
            List<Map> raw = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("institutionId", testContext.getInstitutionId()))
                    .retrieveSync("getAllStudentsInInstitution")
                    .toEntityList(Map.class);
            log.info("getAllStudentsInInstitution returned {} students", raw == null ? "null" : raw.size());
            lastGetStudentsResponse = (raw == null ? List.<Map<String, Object>>of() :
                    raw.stream().map(m -> (Map<String, Object>) m).toList());
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (Exception e) {
            log.error("Unexpected exception in userQueriesAllStudentsInTheInstitution", e);
            throw e;
        }
    }

    @Then("the students response should contain {int} students")
    public void theStudentsResponseShouldContainStudents(int expected) {
        assertThat(lastGetStudentsResponse).hasSize(expected);
    }

    @Then("the students response should be empty")
    public void theStudentsResponseShouldBeEmpty() {
        assertThat(lastGetStudentsResponse).isNotNull().isEmpty();
    }

    @When("user updates the noted student with name {string}")
    @SuppressWarnings("unchecked")
    public void userUpdatesNotedStudentWithName(String newName) {
        String query = findGraphqlQueryFromFiles("update-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getStudentId(),
                "institutionId", testContext.getInstitutionId(),
                "name", newName
        );
        try {
            lastUpdateStudentResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateStudentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @When("user tries to update a student with a random id and name {string}")
    public void userTriesToUpdateAStudentWithRandomId(String newName) {
        String query = findGraphqlQueryFromFiles("update-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId(),
                "name", newName
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateStudentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @When("user deletes the noted student by id")
    public void userDeletesNotedStudentById() {
        String query = findGraphqlQueryFromFiles("delete-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getStudentId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            lastDeleteStudentResponse = testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteStudentById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
    }

    @Then("the delete student response should be true")
    public void theDeleteStudentResponseShouldBeTrue() {
        assertThat(lastDeleteStudentResponse).isTrue();
    }

    @Then("querying the deleted student by id should fail with error code {string}")
    public void queryingDeletedStudentShouldFail(String errorCode) {
        String query = findGraphqlQueryFromFiles("get-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", testContext.getStudentId(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("getStudentById")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        }
        assertThat(testContext.isRequestFailed()).isTrue();
        assertThat(testContext.getLastErrorMessage()).isEqualTo(errorCode);
    }

    @When("user tries to delete a student with a random id")
    public void userTriesToDeleteStudentWithRandomId() {
        String query = findGraphqlQueryFromFiles("delete-student-by-id.graphql");
        Map<String, Object> input = Map.of(
                "id", UUID.randomUUID().toString(),
                "institutionId", testContext.getInstitutionId()
        );
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("deleteStudentById")
                    .toEntity(Boolean.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleErr(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    private void handleErr(FieldAccessException e) {
        testContext.setRequestFailed(true);
        testContext.setLastErrorMessage(e.getMessage());
        var errors = e.getResponse().getErrors();
        if (!errors.isEmpty()) {
            var ext = errors.getFirst().getExtensions();
            if (ext != null && ext.containsKey("error_code")) {
                testContext.setLastErrorMessage(ext.get("error_code").toString());
            }
        }
    }

    /**
     * Ensures a level exists for the current institution; inserts one via JDBC if missing
     * (template data is truncated by DatabaseCleanupHooks between scenarios).
     */
    private String ensureLevelExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM levels WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.getFirst();
        }
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO levels (id, version, tenant_id, name, category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test Level', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }

    /**
     * Ensures a school exists for the current institution; inserts one via JDBC if missing.
     */
    private String ensureSchoolExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM schools WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.getFirst();
        }
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO schools (id, version, tenant_id, name, school_category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test School', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }
}
