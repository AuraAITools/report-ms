package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.exceptions.DuplicateResourceException;
import com.aura.core.repositories.OutletRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Create outlet use case")
@ExtendWith(MockitoExtension.class)
class CreateOutletUseCaseImplTest {

    @Mock
    private OutletRepository outletRepository;

    @InjectMocks
    private CreateOutletUseCaseImpl useCase;

    @Test
    @DisplayName("Should save and return the outlet when no outlet with the same name exists in the tenant")
    void savesWhenNameIsUnique() {
        Outlet toCreate = Outlet.builder().name("Bukit Timah").tenantId("tenant-1").build();
        Outlet saved = Outlet.builder().id("outlet-1").name("Bukit Timah").tenantId("tenant-1").build();
        when(outletRepository.existsByNameAndTenantId("Bukit Timah", "tenant-1")).thenReturn(false);
        when(outletRepository.save(toCreate)).thenReturn(saved);

        Outlet result = useCase.createOutlet(toCreate);

        assertThat(result).isSameAs(saved);
        verify(outletRepository).save(toCreate);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when an outlet with the same name exists in the tenant")
    void throwsOnDuplicateNameInTenant() {
        Outlet toCreate = Outlet.builder().name("Bukit Timah").tenantId("tenant-1").build();
        when(outletRepository.existsByNameAndTenantId("Bukit Timah", "tenant-1")).thenReturn(true);

        assertThatThrownBy(() -> useCase.createOutlet(toCreate))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Outlet with name 'Bukit Timah' already exists in this institution");
        verify(outletRepository, never()).save(toCreate);
    }
}
