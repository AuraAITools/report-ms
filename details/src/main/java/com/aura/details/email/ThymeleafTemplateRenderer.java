package com.aura.details.email;

import com.aura.core.repositories.email.TemplateRenderer;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.expression.ThymeleafEvaluationContext;

@RequiredArgsConstructor
public class ThymeleafTemplateRenderer implements TemplateRenderer {

    private final TemplateEngine emailTemplateEngine;
    private final ApplicationContext applicationContext;

    @Override
    public RenderedEmail render(String templateName, Map<String, Object> variables) {
        Context ctx = new Context();
        // Emails render on the async email executor, outside any web request. The
        // SpringStandardDialect evaluates ${...} expressions with SpEL and requires a
        // ThymeleafEvaluationContext backed by the ApplicationContext; Spring MVC sets
        // it up per request, but on a non-web thread it is absent and rendering fails
        // with "Application Context cannot be null". Provide it explicitly so templates
        // render on any thread.
        ctx.setVariable(
                ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME,
                new ThymeleafEvaluationContext(applicationContext, null));
        ctx.setVariables(variables);
        String html = emailTemplateEngine.process(templateName, ctx);
        String text = Jsoup.parse(html).text();
        return new RenderedEmail(html, text);
    }
}
