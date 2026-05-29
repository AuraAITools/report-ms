package com.aura.tests.component.email;

import com.aura.core.entities.email.EmailMessage;
import com.aura.core.repositories.email.EmailGateway;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Test-profile EmailGateway that records every send to an in-memory list.
 * Step definitions assert against this via {@link EmailTestProbe}.
 */
public class FakeEmailGateway implements EmailGateway {

    private final List<EmailMessage> sent = new CopyOnWriteArrayList<>();

    @Override
    public String send(EmailMessage message) {
        sent.add(message);
        return "fake-msg-" + UUID.randomUUID();
    }

    public List<EmailMessage> getSent() {
        return List.copyOf(sent);
    }

    public void reset() {
        sent.clear();
    }
}
