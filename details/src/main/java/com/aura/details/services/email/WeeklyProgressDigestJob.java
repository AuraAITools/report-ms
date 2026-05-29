package com.aura.details.services.email;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Fires the weekly progress digest. The job is feature-flagged via
 * aura.email.weekly-digest.enabled and ShedLock-guarded so only one app
 * replica runs the cron at a time.
 *
 * <p>The aggregation step (which parents are eligible, how progress is
 * computed) is a follow-up spec — for v1 the job is a hand-cranked
 * scaffold that logs and does no event fan-out unless explicitly enabled.
 */
@Slf4j
@RequiredArgsConstructor
public class WeeklyProgressDigestJob {

    private static final DateTimeFormatter WEEK_LABEL = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final boolean enabled;

    @Scheduled(cron = "${aura.email.weekly-digest.cron}", zone = "${aura.email.weekly-digest.zone}")
    @SchedulerLock(name = "weeklyProgressDigest", lockAtMostFor = "PT2H", lockAtLeastFor = "PT1M")
    public void run() {
        if (!enabled) {
            log.info("Weekly progress digest job is disabled — skipping run");
            return;
        }
        String weekLabel = LocalDate.now().minusDays(7).with(DayOfWeek.MONDAY).format(WEEK_LABEL);
        log.info("Weekly progress digest job firing for week={}", weekLabel);
        // Real fan-out (per parent per institution) is implemented when the
        // aggregation logic ships. For v1 we stop here so deploying the
        // scaffold is safe even with the flag flipped on accidentally.
    }
}
