package com.aura.app.configs;

import com.aura.core.repositories.email.TemplateRenderer;
import com.aura.core.usecases.email.contexts.WeeklyProgressContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.thymeleaf.TemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Renders the real production email templates through the engine built by
 * {@link EmailConfig} on a dedicated executor thread (no web request, no
 * request-bound ThymeleafEvaluationContext) — the same conditions as the
 * async "emailExecutor" used by the email pipeline. Guards against
 * "Application Context cannot be null" regressions.
 */
@DisplayName("Email template rendering off the web thread")
class EmailTemplateRenderingTest {

    private TemplateRenderer renderer;
    private ExecutorService emailExecutor;

    @BeforeEach
    void setUp() {
        EmailConfig config = new EmailConfig();
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        TemplateEngine engine = config.emailTemplateEngine(applicationContext);
        renderer = config.templateRenderer(engine, applicationContext);
        emailExecutor = Executors.newSingleThreadExecutor(
                Thread.ofVirtual().name("email-test-").factory());
    }

    @AfterEach
    void tearDown() {
        emailExecutor.shutdownNow();
    }

    private TemplateRenderer.RenderedEmail renderOffWebThread(String template, Map<String, Object> variables)
            throws Exception {
        return emailExecutor
                .submit(() -> renderer.render(template, variables))
                .get(15, TimeUnit.SECONDS);
    }

    @DisplayName("Should render parent-welcome to non-empty HTML on a non-web thread")
    @Test
    void rendersParentWelcomeOffWebThread() throws Exception {
        TemplateRenderer.RenderedEmail rendered = renderOffWebThread("parent-welcome", Map.of(
                "firstName", "Alice",
                "institutionName", "Aura Academy",
                "mobileAppDeeplink", "https://app.aura.test/onboarding?role=parent"));

        assertThat(rendered.html())
                .isNotBlank()
                .contains("Alice")
                .contains("Aura Academy")
                .contains("https://app.aura.test/onboarding?role=parent");
        assertThat(rendered.text()).isNotBlank().contains("Alice");
    }

    @DisplayName("Should render educator-welcome to non-empty HTML on a non-web thread")
    @Test
    void rendersEducatorWelcomeOffWebThread() throws Exception {
        TemplateRenderer.RenderedEmail rendered = renderOffWebThread("educator-welcome", Map.of(
                "firstName", "Bob",
                "institutionName", "Aura Academy",
                "mobileAppDeeplink", "https://app.aura.test/onboarding?role=educator"));

        assertThat(rendered.html())
                .isNotBlank()
                .contains("Bob")
                .contains("Aura Academy")
                .contains("https://app.aura.test/onboarding?role=educator");
        assertThat(rendered.text()).isNotBlank().contains("Bob");
    }

    @DisplayName("Should render outlet-admin-assigned to non-empty HTML on a non-web thread")
    @Test
    void rendersOutletAdminAssignedOffWebThread() throws Exception {
        TemplateRenderer.RenderedEmail rendered = renderOffWebThread("outlet-admin-assigned", Map.of(
                "firstName", "Carol",
                "outletName", "Downtown Outlet",
                "institutionName", "Aura Academy"));

        assertThat(rendered.html())
                .isNotBlank()
                .contains("Carol")
                .contains("Downtown Outlet")
                .contains("Aura Academy");
        assertThat(rendered.text()).isNotBlank().contains("Downtown Outlet");
    }

    @DisplayName("Should render weekly-progress including student record fields on a non-web thread")
    @Test
    void rendersWeeklyProgressOffWebThread() throws Exception {
        List<WeeklyProgressContext.StudentSummary> students = List.of(
                WeeklyProgressContext.StudentSummary.builder()
                        .name("Dana")
                        .lessonsCompleted(3)
                        .highlight("Great improvement in algebra")
                        .build(),
                WeeklyProgressContext.StudentSummary.builder()
                        .name("Evan")
                        .lessonsCompleted(2)
                        .highlight("Strong reading session")
                        .build());

        TemplateRenderer.RenderedEmail rendered = renderOffWebThread("weekly-progress", Map.of(
                "parentFirstName", "Alice",
                "institutionName", "Aura Academy",
                "weekLabel", "Week of 8 June 2026",
                "students", students));

        assertThat(rendered.html())
                .isNotBlank()
                .contains("Alice")
                .contains("Week of 8 June 2026")
                .contains("Dana")
                .contains("Great improvement in algebra")
                .contains("Evan");
        assertThat(rendered.text()).isNotBlank().contains("Dana").contains("Evan");
    }
}
