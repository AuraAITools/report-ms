package com.aura.core.repositories.email;

import com.aura.core.entities.email.EmailMessage;
import com.aura.core.exceptions.EmailDeliveryException;

public interface EmailGateway {
    /**
     * Sends the message via the underlying provider. Returns the provider's
     * message id on success. Throws EmailDeliveryException on permanent failure
     * (the implementation is responsible for any internal retry).
     */
    String send(EmailMessage message) throws EmailDeliveryException;
}
