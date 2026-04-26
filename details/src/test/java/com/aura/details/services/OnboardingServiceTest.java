package com.aura.details.services;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Onboarding service")
@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {

    @Mock private OnboardInstitutionUseCase onboardInstitutionUseCase;

    @InjectMocks private OnboardingService onboardingService;

    @DisplayName("Should delegate onboarding to use case and return created institution")
    @Test
    void onboard_delegatesToUseCase() {
        Institution input = Institution.builder().name("Test School").build();
        Institution created = Institution.builder().id("inst-123").name("Test School").build();
        when(onboardInstitutionUseCase.onboard(input)).thenReturn(created);

        Institution result = onboardingService.onboard(input);

        assertThat(result).isEqualTo(created);
        verify(onboardInstitutionUseCase).onboard(input);
    }
}
