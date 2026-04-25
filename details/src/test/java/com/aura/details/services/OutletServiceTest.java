package com.aura.details.services;

import com.aura.core.clients.GroupAuthorizationService;
import com.aura.core.clients.ResourceAuthorizationService;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Outlet service")
@ExtendWith(MockitoExtension.class)
class OutletServiceTest {

    @Mock
    private CreateOutletUseCase createOutletUseCase;
    @Mock
    private OutletQueryUseCase outletQueryUseCase;
    @Mock
    private ResourceAuthorizationService resourceAuthorizationService;
    @Mock
    private GroupAuthorizationService groupAuthorizationService;

    private OutletService outletService;

    @BeforeEach
    void setUp() {
        outletService = new OutletService(createOutletUseCase, outletQueryUseCase, resourceAuthorizationService, groupAuthorizationService);
    }

    @DisplayName("Should register UMA resource and return created outlet")
    @Test
    void createOutlet_registersUmaResourceAndReturnsCreatedOutlet() {
        Outlet input = Outlet.builder().name("Test Outlet").build();
        Outlet created = Outlet.builder().id("outlet-123").name("Test Outlet").tenantId("tenant-456").build();
        when(createOutletUseCase.createOutlet(any())).thenReturn(created);

        Outlet result = outletService.createOutlet(input);

        assertThat(result).isEqualTo(created);
        verify(createOutletUseCase).createOutlet(input);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Set<String>> scopesCaptor = ArgumentCaptor.forClass(Set.class);

        verify(resourceAuthorizationService).registerResource(
                nameCaptor.capture(), typeCaptor.capture(), any(), scopesCaptor.capture());

        assertThat(nameCaptor.getValue()).isEqualTo("outlet:outlet-123");
        assertThat(typeCaptor.getValue()).isEqualTo("urn:aura:outlet");
        assertThat(scopesCaptor.getValue()).containsExactlyInAnyOrder("view", "manage", "delegate");
    }
}
