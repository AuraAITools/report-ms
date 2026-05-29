package com.aura.details.services.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Weekly progress digest job")
class WeeklyProgressDigestJobTest {

    @DisplayName("Should be a no-op when feature flag is disabled")
    @Test
    void noOpWhenDisabled() {
        WeeklyProgressDigestJob job = new WeeklyProgressDigestJob(false);

        job.run();
    }

    @DisplayName("Should run without throwing when enabled (scaffold only — no fan-out yet)")
    @Test
    void runsWithoutErrorWhenEnabled() {
        WeeklyProgressDigestJob job = new WeeklyProgressDigestJob(true);

        job.run();

        // v1 scaffold logs and stops; real fan-out is a follow-up.
    }
}
