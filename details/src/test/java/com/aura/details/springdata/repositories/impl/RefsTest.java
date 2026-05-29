package com.aura.details.springdata.repositories.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


import com.aura.details.springdata.models.outlets.OutletModel;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Refs helper")
@ExtendWith(MockitoExtension.class)
class RefsTest {

    @Mock
    private EntityManager entityManager;

    @DisplayName("Should return null when id is null without touching the entity manager")
    @Test
    void returnsNullForNullId() {
        assertThat(Refs.ref(entityManager, OutletModel.class, null)).isNull();
        verifyNoInteractions(entityManager);
    }

    @DisplayName("Should delegate to entityManager.getReference with the parsed UUID")
    @Test
    void delegatesToGetReferenceWithParsedUuid() {
        String id = "062ba24f-6c8e-41f6-982f-c6d65522af99";
        OutletModel proxy = OutletModel.builder().build();
        when(entityManager.getReference(OutletModel.class, UUID.fromString(id))).thenReturn(proxy);

        OutletModel result = Refs.ref(entityManager, OutletModel.class, id);

        assertThat(result).isSameAs(proxy);
    }
}
