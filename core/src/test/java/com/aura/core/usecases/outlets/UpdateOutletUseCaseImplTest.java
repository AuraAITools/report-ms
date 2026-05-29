package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Update outlet use case")
@ExtendWith(MockitoExtension.class)
class UpdateOutletUseCaseImplTest {

    @Mock
    private OutletRepository outletRepository;
    @InjectMocks
    private UpdateOutletUseCaseImpl useCase;

    @DisplayName("Should overwrite editable fields, preserve id/tenantId/audit fields, and save")
    @Test
    void updateOutlet_overwritesEditableFieldsAndSaves() {
        Outlet existing = Outlet.builder()
                .id("outlet-1")
                .name("Old Name")
                .address("Old Address")
                .postalCode("000000")
                .contactNumber("11111111")
                .description("Old Desc")
                .email("old@test.com")
                .tenantId("tenant-1")
                .createdBy("creator-user")
                .build();
        Outlet input = Outlet.builder()
                .id("outlet-1")
                .tenantId("tenant-1")
                .name("New Name")
                .address("New Address")
                .postalCode("999999")
                .contactNumber("99999999")
                .description("New Desc")
                .email("new@test.com")
                .build();
        when(outletRepository.findById("outlet-1")).thenReturn(existing);
        when(outletRepository.save(any(Outlet.class))).thenAnswer(inv -> inv.getArgument(0));

        Outlet result = useCase.updateOutlet(input);

        ArgumentCaptor<Outlet> savedCaptor = ArgumentCaptor.forClass(Outlet.class);
        verify(outletRepository).save(savedCaptor.capture());
        Outlet saved = savedCaptor.getValue();

        assertThat(saved.getId()).isEqualTo("outlet-1");
        assertThat(saved.getTenantId()).isEqualTo("tenant-1");
        assertThat(saved.getCreatedBy()).isEqualTo("creator-user");
        assertThat(saved.getName()).isEqualTo("New Name");
        assertThat(saved.getAddress()).isEqualTo("New Address");
        assertThat(saved.getPostalCode()).isEqualTo("999999");
        assertThat(saved.getContactNumber()).isEqualTo("99999999");
        assertThat(saved.getDescription()).isEqualTo("New Desc");
        assertThat(saved.getEmail()).isEqualTo("new@test.com");
        assertThat(result).isSameAs(saved);
    }

    @DisplayName("Should return null and skip save when outlet does not exist")
    @Test
    void updateOutlet_returnsNullWhenNotFound() {
        Outlet input = Outlet.builder().id("missing").tenantId("tenant-1").name("Whatever").build();
        when(outletRepository.findById("missing")).thenReturn(null);

        Outlet result = useCase.updateOutlet(input);

        assertThat(result).isNull();
        verify(outletRepository).findById("missing");
        verifyNoMoreInteractions(outletRepository);
    }

    @DisplayName("Should return null and skip save when input institutionId does not match the loaded outlet's tenant")
    @Test
    void updateOutlet_returnsNullWhenTenantMismatch() {
        Outlet existing = Outlet.builder().id("outlet-1").tenantId("tenant-A").name("Whatever").build();
        Outlet input = Outlet.builder().id("outlet-1").tenantId("tenant-B").name("New Name").build();
        when(outletRepository.findById("outlet-1")).thenReturn(existing);

        Outlet result = useCase.updateOutlet(input);

        assertThat(result).isNull();
        verify(outletRepository).findById("outlet-1");
        verifyNoMoreInteractions(outletRepository);
    }
}
