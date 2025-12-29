package com.aura.core.usecases.onboarding;

import com.aura.core.entities.institutions.Institution;

public interface OnboardingStrategy {
    Institution onboard();
}
