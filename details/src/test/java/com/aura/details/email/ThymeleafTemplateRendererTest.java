package com.aura.details.email;

import com.aura.core.repositories.email.TemplateRenderer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Thymeleaf template renderer")
class ThymeleafTemplateRendererTest {

    private ThymeleafTemplateRenderer renderer;

    @BeforeEach
    void setUp() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/email/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(false);
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        renderer = new ThymeleafTemplateRenderer(engine);
    }

    @DisplayName("Should substitute variables in HTML")
    @Test
    void rendersVariablesInHtml() {
        TemplateRenderer.RenderedEmail rendered = renderer.render(
                "test-fixture",
                Map.of("name", "Alice", "institution", "Aura Academy"));

        assertThat(rendered.html()).contains("Hello <span>Alice</span>");
        assertThat(rendered.html()).contains("invited to <span>Aura Academy</span>");
    }

    @DisplayName("Should produce a plaintext body stripped of HTML tags")
    @Test
    void producesStrippedTextBody() {
        TemplateRenderer.RenderedEmail rendered = renderer.render(
                "test-fixture",
                Map.of("name", "Alice", "institution", "Aura Academy"));

        assertThat(rendered.text())
                .contains("Hello Alice")
                .contains("Aura Academy")
                .doesNotContain("<")
                .doesNotContain(">");
    }
}
