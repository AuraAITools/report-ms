package com.aura.tests.component.stepdefs;

import com.aura.core.entities.email.EmailMessage;
import com.aura.tests.component.email.FakeEmailGateway;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.function.BooleanSupplier;
import lombok.RequiredArgsConstructor;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for asserting against the FakeEmailGateway.
 *
 * <p>Sends are dispatched via @Async @TransactionalEventListener(AFTER_COMMIT),
 * so assertions poll briefly for the listener thread to flush before failing.
 */
@RequiredArgsConstructor
public class EmailStepDefs {

    private static final long AWAIT_MILLIS = 5000;
    private static final long POLL_MILLIS = 100;

    private final FakeEmailGateway fakeEmailGateway;

    @Then("an email with template {string} should have been sent to {string}")
    public void anEmailShouldHaveBeenSent(String templateName, String recipientEmail) {
        boolean ok = pollUntil(() -> fakeEmailGateway.getSent().stream()
                .anyMatch(m -> templateName.equals(m.templateName()) && recipientEmail.equals(m.recipientEmail())));
        if (!ok) {
            throw new AssertionError(String.format(
                    "expected an email with template=%s recipient=%s; got: %s",
                    templateName, recipientEmail, summary(fakeEmailGateway.getSent())));
        }
    }

    @Then("exactly {int} email(s) should have been sent")
    public void exactlyEmailsShouldHaveBeenSent(int expected) {
        pollUntil(() -> fakeEmailGateway.getSent().size() >= expected);
        assertThat(fakeEmailGateway.getSent()).hasSize(expected);
    }

    @Then("no email should have been sent")
    public void noEmailShouldHaveBeenSent() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        assertThat(fakeEmailGateway.getSent()).isEmpty();
    }

    private static boolean pollUntil(BooleanSupplier condition) {
        long deadline = System.currentTimeMillis() + AWAIT_MILLIS;
        while (System.currentTimeMillis() < deadline) {
            if (condition.getAsBoolean()) {
                return true;
            }
            try {
                Thread.sleep(POLL_MILLIS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return condition.getAsBoolean();
    }

    private static String summary(List<EmailMessage> sent) {
        if (sent.isEmpty()) return "<none>";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < sent.size(); i++) {
            EmailMessage m = sent.get(i);
            if (i > 0) sb.append(", ");
            sb.append(m.templateName()).append("→").append(m.recipientEmail());
        }
        return sb.append("]").toString();
    }
}
