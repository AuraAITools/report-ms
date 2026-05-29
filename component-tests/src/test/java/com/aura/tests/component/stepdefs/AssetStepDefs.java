package com.aura.tests.component.stepdefs;

import com.aura.details.aws.AwsS3Properties;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class AssetStepDefs {

    private final TestContext testContext;
    private final S3Client testS3Client;
    private final AwsS3Properties awsS3Properties;
    private final JdbcTemplate jdbcTemplate;

    private Map<String, Object> lastInstitutionResponse;
    private String lastLogoUrl;

    @Given("the institution has a logo uploaded")
    public void theInstitutionHasALogoUploaded() {
        String institutionId = testContext.getInstitutionId();
        String logoKey = "institutions/" + institutionId + "/logo.png";

        // 1x1 transparent PNG (89 bytes) — content is irrelevant for the test; HTTP GET just needs *something*.
        byte[] pngBytes = new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
                0x08, 0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, (byte) 0xC4,
                (byte) 0x89, 0x00, 0x00, 0x00, 0x0D, 0x49, 0x44, 0x41,
                0x54, 0x78, (byte) 0x9C, 0x62, 0x00, 0x01, 0x00, 0x00,
                0x05, 0x00, 0x01, 0x0D, 0x0A, 0x2D, (byte) 0xB4, 0x00,
                0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44, (byte) 0xAE,
                0x42, 0x60, (byte) 0x82
        };

        testS3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(awsS3Properties.getBucketName())
                        .key(logoKey)
                        .contentType("image/png")
                        .build(),
                RequestBody.fromBytes(pngBytes));

        int updated = jdbcTemplate.update(
                "UPDATE public.institutions SET logo_key = ? WHERE id = ?",
                logoKey, UUID.fromString(institutionId));
        assertThat(updated).isEqualTo(1);

        log.info("Seeded logo at s3://{}/{} and set logo_key on institution {}",
                awsS3Properties.getBucketName(), logoKey, institutionId);
    }

    @When("user queries their institution with logo url")
    public void userQueriesTheirInstitutionWithLogoUrl() {
        String query = findGraphqlQueryFromFiles("get-institution-with-logo.graphql");

        lastInstitutionResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getInstitution")
                .toEntity(Map.class);

        Object logoUrl = lastInstitutionResponse.get("logoUrl");
        lastLogoUrl = logoUrl != null ? logoUrl.toString() : null;
    }

    @Then("the institution response includes no logo url")
    public void theInstitutionResponseIncludesNoLogoUrl() {
        assertThat(lastInstitutionResponse).isNotNull();
        assertThat(lastInstitutionResponse).containsKey("logoUrl");
        assertThat(lastInstitutionResponse.get("logoUrl")).isNull();
    }

    @Then("the institution response includes a logo url")
    public void theInstitutionResponseIncludesALogoUrl() {
        assertThat(lastInstitutionResponse).isNotNull();
        assertThat(lastLogoUrl)
                .as("logoUrl in GraphQL response")
                .isNotBlank()
                .startsWith("http");
    }

    @Then("the logo url returns 200 when fetched")
    public void theLogoUrlReturns200WhenFetched() throws Exception {
        assertThat(lastLogoUrl).as("logoUrl from previous step").isNotBlank();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(URI.create(lastLogoUrl)).GET().build(),
                    HttpResponse.BodyHandlers.discarding());
            assertThat(response.statusCode())
                    .as("HTTP status when fetching presigned URL")
                    .isEqualTo(200);
        }
    }
}
