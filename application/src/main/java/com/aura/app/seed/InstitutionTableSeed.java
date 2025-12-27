package com.aura.app.seed;

import com.aura.details.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.tables.institutions.InstitutionTable;
import com.github.javafaker.Faker;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;

//@Component
//@Profile({"local"})
public class InstitutionTableSeed implements ApplicationRunner {

    private final JpaInstitutionRepository repository;
    private final Faker faker = new Faker();

    public InstitutionTableSeed(JpaInstitutionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        int target = 1000;
        int current = (int) repository.count();
        int diff = target - current;
        List<InstitutionTable> items = IntStream.range(0, diff)
                .mapToObj(i -> {
                    Instant created = faker.date().past(3650, TimeUnit.DAYS).toInstant(); // up to ~10 years ago
                    Instant updated = faker.date().between(Date.from(created), new Date()).toInstant(); // between created and now
                    return InstitutionTable.builder()
                            .id(UUID.randomUUID())
                            .name(faker.company().name() + " " + UUID.randomUUID().toString().substring(0, 8))
                            .email("inst-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com")
                            .uen("UEN" + faker.number().digits(9))
                            .address(faker.address().fullAddress())
                            .contactNumber(faker.phoneNumber().phoneNumber())
                            .tenantId("tenant-" + (i % 10))
                            .createdAt(created)
                            .updatedAt(updated)
                            .build();
                })
                .collect(Collectors.toList());

        repository.saveAll(items);
    }
}
