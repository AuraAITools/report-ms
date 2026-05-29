package com.aura.core.repositories.email;

import java.util.Map;

public interface TemplateRenderer {
    /**
     * Renders the named template with the given variables and returns the
     * HTML body and a plain-text body suitable for the multipart/alternative
     * email payload.
     */
    RenderedEmail render(String templateName, Map<String, Object> variables);

    record RenderedEmail(String html, String text) {
    }
}
