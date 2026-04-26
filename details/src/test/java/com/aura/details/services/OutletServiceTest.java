package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Outlet service")
@ExtendWith(MockitoExtension.class)
class OutletServiceTest {

    @Mock private CreateOutletUseCase createOutletUseCase;
    @Mock private OutletQueryUseCase outletQueryUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private OutletService outletService;

    @DisplayName("Should create outlet and write parent_institution relationship")
    @Test
    void createOutlet_writesParentInstitutionRelationship() {
        Outlet input = Outlet.builder().name("Test Outlet").build();
        Outlet created = Outlet.builder().id("outlet-123").name("Test Outlet").tenantId("tenant-789").build();
        when(createOutletUseCase.createOutlet(any())).thenReturn(created);

        Outlet result = outletService.createOutlet(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "outlet", "outlet-123", "parent_institution", "institution", "tenant-789");
    }
}
