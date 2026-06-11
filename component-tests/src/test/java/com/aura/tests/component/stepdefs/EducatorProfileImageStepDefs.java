package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpServerErrorException;


import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class EducatorProfileImageStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    private String uploadUrl;
    private String fileKey;
    private Map<String, Object> lastEducatorResponse;
    private Throwable lastError;

    @SuppressWarnings("unchecked")
    @And("an educator exists in the institution")
    public void anEducatorExistsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("create-educators-in-account.graphql");
        String levelId = ensureLevelExists();
        Map<String, Object> educator = new HashMap<>();
        educator.put("name", "Profile Educator");
        educator.put("startDate", "2026-01-01");
        educator.put("dateOfBirth", "1990-01-01");
        educator.put("email", "profile-edu-" + UUID.randomUUID() + "@example.com");
        educator.put("employmentType", "FULL_TIME");
        educator.put("courseIds", List.of());
        educator.put("subjectIds", List.of());
        educator.put("outletIds", List.of(testContext.getOutletId()));
        educator.put("levelIds", List.of(levelId));
        educator.put("accountId", testContext.getAccountId());
        educator.put("institutionId", testContext.getInstitutionId());

        Map<String, Object> input = Map.of(
                "institutionId", testContext.getInstitutionId(),
                "educators", List.of(educator));

        List<?> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createEducatorsInAccount")
                .toEntityList(Map.class);

        assertThat(response).isNotEmpty();
        Map<String, Object> first = (Map<String, Object>) response.get(0);
        testContext.setEducatorId(first.get("id").toString());
    }

    @When("user requests a profile image upload for the educator with content type {string}")
    public void userRequestsProfileImageUploadForEducator(String contentType) {
        String query = findGraphqlQueryFromFiles("request-educator-profile-image-upload.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "educatorId", testContext.getEducatorId(),
                        "contentType", contentType)))
                .retrieveSync("requestEducatorProfileImageUpload")
                .toEntity(Map.class);
        uploadUrl = response.get("uploadUrl").toString();
        fileKey = response.get("fileKey").toString();
        assertThat(fileKey).isEqualTo(
                "institutions/" + testContext.getInstitutionId()
                        + "/educators/" + testContext.getEducatorId() + "/profile.png");
    }

    @When("user tries to request a profile image upload for the educator with content type {string}")
    public void userTriesToRequestProfileImageUploadForEducator(String contentType) {
        try {
            userRequestsProfileImageUploadForEducator(contentType);
        } catch (FieldAccessException | HttpServerErrorException e) {
            lastError = e;
        }
    }

    @And("user uploads a {int}x{int} PNG to the educator profile image presigned url")
    public void userUploadsAPngToTheEducatorProfileImagePresignedUrl(int w, int h) throws Exception {
        byte[] bytes = renderPng(w, h);
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(URI.create(uploadUrl))
                            .header("Content-Type", "image/png")
                            .PUT(HttpRequest.BodyPublishers.ofByteArray(bytes))
                            .build(),
                    HttpResponse.BodyHandlers.discarding());
            assertThat(response.statusCode()).isEqualTo(200);
        }
    }

    @And("user confirms the educator profile image upload")
    public void userConfirmsTheEducatorProfileImageUpload() {
        String query = findGraphqlQueryFromFiles("confirm-educator-profile-image-upload.graphql");
        lastEducatorResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "educatorId", testContext.getEducatorId(),
                        "fileKey", fileKey)))
                .retrieveSync("confirmEducatorProfileImageUpload")
                .toEntity(Map.class);
    }

    @And("user tries to confirm the educator profile image upload")
    public void userTriesToConfirmTheEducatorProfileImageUpload() {
        try {
            userConfirmsTheEducatorProfileImageUpload();
        } catch (FieldAccessException | HttpServerErrorException e) {
            lastError = e;
        }
    }

    @Then("the educator response should include a presigned profile image url")
    public void theEducatorResponseShouldIncludeAPresignedProfileImageUrl() {
        assertThat(lastEducatorResponse).isNotNull();
        Object url = lastEducatorResponse.get("profileImageUrl");
        assertThat(url).isNotNull();
        assertThat(url.toString()).startsWith("http");
    }

    @And("the educator profile image url returns 200 when fetched")
    public void theEducatorProfileImageUrlReturns200WhenFetched() throws Exception {
        String url = lastEducatorResponse.get("profileImageUrl").toString();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(URI.create(url)).GET().build(),
                    HttpResponse.BodyHandlers.discarding());
            assertThat(response.statusCode()).isEqualTo(200);
        }
    }

    @Then("the educator request should fail with a content-type rejection error")
    public void theEducatorRequestShouldFailWithAContentTypeRejectionError() {
        assertThat(lastError).isNotNull();
        assertThat(lastError.getMessage().toLowerCase()).contains("content type");
    }

    @Then("the educator request should fail with an aspect-ratio rejection error")
    public void theEducatorRequestShouldFailWithAnAspectRatioRejectionError() {
        assertThat(lastError).isNotNull();
        assertThat(lastError.getMessage().toLowerCase()).contains("aspect");
    }

    @And("no profile image key should be persisted on the educator")
    public void noProfileImageKeyShouldBePersistedOnTheEducator() {
        String key = jdbcTemplate.queryForObject(
                "SELECT profile_image_key FROM public.educators WHERE id = ?",
                String.class, UUID.fromString(testContext.getEducatorId()));
        assertThat(key).isNull();
    }

    private String ensureLevelExists() {
        String tenantId = testContext.getInstitutionId();
        List<String> existing = jdbcTemplate.queryForList(
                "SELECT id::text FROM levels WHERE tenant_id = ? LIMIT 1",
                String.class, tenantId);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(
                "INSERT INTO levels (id, version, tenant_id, name, category, created_at, created_by, updated_by) "
                        + "VALUES (?::uuid, 0, ?, 'Test Level', 'PRIMARY', now(), 'SYSTEM', 'SYSTEM')",
                id, tenantId);
        return id;
    }

    private static byte[] renderPng(int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, w, h);
        g.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }
}
