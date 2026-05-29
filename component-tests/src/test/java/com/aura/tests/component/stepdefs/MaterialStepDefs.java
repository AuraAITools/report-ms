package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class MaterialStepDefs {

    private static final byte[] FAKE_PDF_BYTES = new byte[]{
            '%', 'P', 'D', 'F', '-', '1', '.', '4', '\n', '%', '\n',
            'E', 'O', 'F'
    };

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    private Map<String, Object> lastMaterialResponse;

    @When("user creates a material in the institution with")
    public void userCreatesAMaterialInTheInstitutionWith(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().get(0);
        createMaterial(row.get("name"), row.get("description"), null, List.of());
    }

    @When("user requests a material upload with content type {string}")
    public void userRequestsAMaterialUploadWithContentType(String contentType) {
        String query = findGraphqlQueryFromFiles("request-material-upload.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "contentType", contentType
                )))
                .retrieveSync("requestMaterialUpload")
                .toEntity(Map.class);

        assertThat(response).containsKey("uploadUrl");
        assertThat(response).containsKey("fileKey");

        testContext.setMaterialUploadUrl(response.get("uploadUrl").toString());
        testContext.setMaterialFileKey(response.get("fileKey").toString());
        assertThat(testContext.getMaterialFileKey())
                .startsWith("institutions/" + testContext.getInstitutionId() + "/materials/");
    }

    @And("user uploads a PDF to the presigned url")
    public void userUploadsAPDFToThePresignedUrl() throws Exception {
        assertThat(testContext.getMaterialUploadUrl()).isNotBlank();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(URI.create(testContext.getMaterialUploadUrl()))
                            .header("Content-Type", "application/pdf")
                            .PUT(HttpRequest.BodyPublishers.ofByteArray(FAKE_PDF_BYTES))
                            .build(),
                    HttpResponse.BodyHandlers.discarding());
            assertThat(response.statusCode())
                    .as("HTTP status when uploading to presigned url")
                    .isEqualTo(200);
        }
    }

    @And("user creates a material in the institution with the uploaded file and name {string}")
    public void userCreatesAMaterialWithTheUploadedFileAndName(String name) {
        createMaterial(name, null, testContext.getMaterialFileKey(), List.of());
    }

    @When("user creates a material in the institution linked to the existing topic with name {string}")
    public void userCreatesAMaterialLinkedToExistingTopicWithName(String name) {
        assertThat(testContext.getTopicId()).as("a topic must exist before this step").isNotNull();
        createMaterial(name, null, null, List.of(testContext.getTopicId()));
    }

    @Then("the material should be created successfully with no file url")
    public void theMaterialShouldBeCreatedSuccessfullyWithNoFileUrl() {
        assertThat(lastMaterialResponse).isNotNull();
        assertThat(lastMaterialResponse).containsKey("fileUrl");
        assertThat(lastMaterialResponse.get("fileUrl")).isNull();
    }

    @And("the material response should include description {string}")
    public void theMaterialResponseShouldIncludeDescription(String description) {
        assertThat(lastMaterialResponse.get("description")).isEqualTo(description);
    }

    @Then("the material response should include a presigned file url")
    public void theMaterialResponseShouldIncludeAPresignedFileUrl() {
        assertThat(lastMaterialResponse).isNotNull();
        Object fileUrl = lastMaterialResponse.get("fileUrl");
        assertThat(fileUrl).as("fileUrl in createMaterial response").isNotNull();
        assertThat(fileUrl.toString()).startsWith("http");
    }

    @And("the material file url returns 200 when fetched")
    public void theMaterialFileUrlReturns200WhenFetched() throws Exception {
        String fileUrl = lastMaterialResponse.get("fileUrl").toString();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(URI.create(fileUrl)).GET().build(),
                    HttpResponse.BodyHandlers.discarding());
            assertThat(response.statusCode())
                    .as("HTTP status when fetching presigned material url")
                    .isEqualTo(200);
        }
    }

    @And("the material should be linked to the topic in the database")
    public void theMaterialShouldBeLinkedToTheTopicInTheDatabase() {
        UUID materialId = UUID.fromString(testContext.getMaterialId());
        UUID topicId = UUID.fromString(testContext.getTopicId());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.material_topic_links WHERE material_id = ? AND topic_id = ?",
                Integer.class, materialId, topicId);
        assertThat(count).as("material_topic_links rows").isEqualTo(1);
    }

    private void createMaterial(String name, String description, String fileKey, List<String> topicIds) {
        String query = findGraphqlQueryFromFiles("create-material-in-institution.graphql");

        java.util.Map<String, Object> inputMap = new java.util.HashMap<>();
        inputMap.put("institutionId", testContext.getInstitutionId());
        inputMap.put("name", name);
        inputMap.put("description", description);
        inputMap.put("fileKey", fileKey);
        inputMap.put("topicIds", topicIds);

        lastMaterialResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", inputMap))
                .retrieveSync("createMaterialInInstitution")
                .toEntity(Map.class);

        assertThat(lastMaterialResponse).isNotNull();
        assertThat(lastMaterialResponse.get("id")).isNotNull();
        testContext.setMaterialId(lastMaterialResponse.get("id").toString());
    }
}
