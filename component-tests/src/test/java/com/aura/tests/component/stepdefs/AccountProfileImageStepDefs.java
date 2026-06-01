package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
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
public class AccountProfileImageStepDefs {

    private final TestContext testContext;
    private final JdbcTemplate jdbcTemplate;

    private String uploadUrl;
    private String fileKey;
    private Map<String, Object> lastAccountResponse;
    private Throwable lastError;

    @Given("an account exists in the institution")
    public void anAccountExistsInTheInstitution() {
        String query = findGraphqlQueryFromFiles("create-account.graphql");
        Map<String, Object> input = new HashMap<>();
        input.put("institutionId", testContext.getInstitutionId());
        input.put("email", "profile-img-" + UUID.randomUUID() + "@example.com");
        input.put("firstName", "Profile");
        input.put("lastName", "User");
        input.put("contact", 12345678);
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("createAccount")
                .toEntity(Map.class);
        testContext.setAccountId(response.get("id").toString());
    }

    @When("user requests a profile image upload for the account with content type {string}")
    public void userRequestsProfileImageUpload(String contentType) {
        String query = findGraphqlQueryFromFiles("request-account-profile-image-upload.graphql");
        Map<String, Object> response = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "accountId", testContext.getAccountId(),
                        "contentType", contentType)))
                .retrieveSync("requestAccountProfileImageUpload")
                .toEntity(Map.class);
        uploadUrl = response.get("uploadUrl").toString();
        fileKey = response.get("fileKey").toString();
        assertThat(fileKey).isEqualTo(
                "institutions/" + testContext.getInstitutionId()
                        + "/accounts/" + testContext.getAccountId() + "/profile.png");
    }

    @When("user tries to request a profile image upload for the account with content type {string}")
    public void userTriesToRequestProfileImageUpload(String contentType) {
        try {
            userRequestsProfileImageUpload(contentType);
        } catch (FieldAccessException | HttpServerErrorException e) {
            lastError = e;
        }
    }

    @And("user uploads a {int}x{int} PNG to the profile image presigned url")
    public void userUploadsAPngToTheProfileImagePresignedUrl(int w, int h) throws Exception {
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

    @And("user confirms the profile image upload")
    public void userConfirmsTheProfileImageUpload() {
        String query = findGraphqlQueryFromFiles("confirm-account-profile-image-upload.graphql");
        lastAccountResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "accountId", testContext.getAccountId(),
                        "fileKey", fileKey)))
                .retrieveSync("confirmAccountProfileImageUpload")
                .toEntity(Map.class);
    }

    @And("user tries to confirm the profile image upload")
    public void userTriesToConfirmTheProfileImageUpload() {
        try {
            userConfirmsTheProfileImageUpload();
        } catch (FieldAccessException | HttpServerErrorException e) {
            lastError = e;
        }
    }

    @Then("the account response should include a presigned profile image url")
    public void theAccountResponseShouldIncludeAPresignedProfileImageUrl() {
        assertThat(lastAccountResponse).isNotNull();
        Object url = lastAccountResponse.get("profileImageUrl");
        assertThat(url).isNotNull();
        assertThat(url.toString()).startsWith("http");
    }

    @And("the profile image url returns 200 when fetched")
    public void theProfileImageUrlReturns200WhenFetched() throws Exception {
        String url = lastAccountResponse.get("profileImageUrl").toString();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<Void> response = client.send(
                    HttpRequest.newBuilder(URI.create(url)).GET().build(),
                    HttpResponse.BodyHandlers.discarding());
            assertThat(response.statusCode()).isEqualTo(200);
        }
    }

    @Then("the request should fail with a content-type rejection error")
    public void theRequestShouldFailWithAContentTypeRejectionError() {
        assertThat(lastError).isNotNull();
        assertThat(lastError.getMessage().toLowerCase()).contains("content type");
    }

    @Then("the request should fail with an aspect-ratio rejection error")
    public void theRequestShouldFailWithAnAspectRatioRejectionError() {
        assertThat(lastError).isNotNull();
        assertThat(lastError.getMessage().toLowerCase()).contains("aspect");
    }

    @And("no profile image key should be persisted on the account")
    public void noProfileImageKeyShouldBePersistedOnTheAccount() {
        String key = jdbcTemplate.queryForObject(
                "SELECT profile_image_key FROM public.accounts WHERE id = ?",
                String.class, UUID.fromString(testContext.getAccountId()));
        assertThat(key).isNull();
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
