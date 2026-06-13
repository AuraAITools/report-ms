package com.aura.app.configs;

import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.email.EmailGateway;
import com.aura.core.repositories.email.TemplateRenderer;
import com.aura.core.usecases.email.StudentProgressAggregator;
import com.aura.details.email.PlaceholderStudentProgressAggregator;
import com.aura.details.email.SesEmailGateway;
import com.aura.details.email.ThymeleafTemplateRenderer;
import com.aura.details.services.email.EmailService;
import com.aura.details.services.email.WeeklyProgressDigestJob;
import com.aura.details.services.email.listeners.EmailEventListeners;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import javax.sql.DataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableAsync
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT2H")
public class EmailConfig {

    @Bean
    EmailGateway emailGateway(
            JavaMailSender mailSender,
            @Value("${aura.email.from-address}") String fromAddress,
            @Value("${aura.email.from-display-name}") String fromDisplayName,
            @Value("${aura.email.retry-delay-millis:5000}") long retryDelayMillis
    ) {
        return new SesEmailGateway(mailSender, fromAddress, fromDisplayName, retryDelayMillis);
    }

    @Bean
    TemplateEngine emailTemplateEngine(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        // The resolver is created manually (not as a bean), so ApplicationContextAware
        // injection never runs — set the context explicitly or template resolution fails.
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:templates/email/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(true);
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    @Bean
    TemplateRenderer templateRenderer(TemplateEngine emailTemplateEngine, ApplicationContext applicationContext) {
        return new ThymeleafTemplateRenderer(emailTemplateEngine, applicationContext);
    }

    @Bean(name = "emailExecutor")
    Executor emailExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("email-");
        executor.setVirtualThreads(true);
        return executor;
    }

    @Bean
    StudentProgressAggregator studentProgressAggregator(AccountRepository accountRepository) {
        return new PlaceholderStudentProgressAggregator(accountRepository);
    }

    @Bean
    EmailEventListeners emailEventListeners(EmailService emailService,
                                            StudentProgressAggregator studentProgressAggregator) {
        return new EmailEventListeners(emailService, studentProgressAggregator);
    }

    @Bean
    LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new org.springframework.jdbc.core.JdbcTemplate(dataSource))
                .usingDbTime()
                .build());
    }

    @Bean
    WeeklyProgressDigestJob weeklyProgressDigestJob(
            @Value("${aura.email.weekly-digest.enabled:false}") boolean enabled
    ) {
        return new WeeklyProgressDigestJob(enabled);
    }
}
