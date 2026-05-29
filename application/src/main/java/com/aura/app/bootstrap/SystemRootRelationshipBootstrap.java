package com.aura.app.bootstrap;

import com.aura.core.clients.RelationshipAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Slf4j
@RequiredArgsConstructor
public class SystemRootRelationshipBootstrap implements ApplicationRunner {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Bootstrapping system:aura#user_group@system_user_group:aura");
        relationshipService.writeRelationship("system", "aura", "user_group", "system_user_group", "aura");
    }
}
