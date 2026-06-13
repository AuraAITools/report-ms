package com.aura.app.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.aura.app.codegen.types.OutletResponse;
import com.aura.core.entities.outlets.Outlet;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OutletMapper")
class OutletMapperTest {

    @Test
    @DisplayName("Should map audit fields (createdBy/updatedBy/createdAt/updatedAt) onto the outlet response")
    void mapsAuditFieldsToDto() {
        Instant createdAt = Instant.parse("2026-06-10T09:00:00Z");
        Instant updatedAt = Instant.parse("2026-06-11T10:00:00Z");
        Outlet outlet = Outlet.builder()
                .id("outlet-1")
                .name("Bukit Timah")
                .tenantId("tenant-1")
                .createdAt(createdAt)
                .createdBy("creator-user-id")
                .updatedAt(updatedAt)
                .updatedBy("updater-user-id")
                .build();

        OutletResponse response = OutletMapper.INSTANCE.toDto(outlet);

        assertThat(response.getId()).isEqualTo("outlet-1");
        assertThat(response.getName()).isEqualTo("Bukit Timah");
        assertThat(response.getCreatedBy()).isNotNull();
        assertThat(response.getCreatedBy().getUserId()).isEqualTo("creator-user-id");
        assertThat(response.getUpdatedBy()).isNotNull();
        assertThat(response.getUpdatedBy().getUserId()).isEqualTo("updater-user-id");
        assertThat(response.getCreatedAt()).isEqualTo(createdAt.toEpochMilli());
        assertThat(response.getUpdatedAt()).isEqualTo(updatedAt.toEpochMilli());
    }

    @Test
    @DisplayName("Should map updatedBy to null when the outlet has never been updated")
    void mapsNullUpdatedByToNull() {
        Outlet outlet = Outlet.builder()
                .id("outlet-1")
                .name("Bukit Timah")
                .createdAt(Instant.parse("2026-06-10T09:00:00Z"))
                .createdBy("creator-user-id")
                .build();

        OutletResponse response = OutletMapper.INSTANCE.toDto(outlet);

        assertThat(response.getCreatedBy()).isNotNull();
        assertThat(response.getUpdatedBy()).isNull();
    }
}
