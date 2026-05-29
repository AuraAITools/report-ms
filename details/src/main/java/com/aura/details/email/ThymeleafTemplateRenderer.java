package com.aura.details.email;

import com.aura.core.repositories.email.TemplateRenderer;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
public class ThymeleafTemplateRenderer implements TemplateRenderer {

    private final TemplateEngine emailTemplateEngine;

    @Override
    public RenderedEmail render(String templateName, Map<String, Object> variables) {
        Context ctx = new Context();
        ctx.setVariables(variables);
        String html = emailTemplateEngine.process(templateName, ctx);
        String text = Jsoup.parse(html).text();
        return new RenderedEmail(html, text);
    }
}
