package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OutletRoomMapper")
class OutletRoomMapperTest {

    private final OutletRoomMapper mapper = OutletRoomMapper.INSTANCE;

    @DisplayName("Should map OutletRoomModel.outlet.id into OutletRoom.outletId without traversing back into Outlet")
    @Test
    void toDomainEntity_extractsOutletIdFromModel() {
        UUID outletId = UUID.randomUUID();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        OutletRoomModel model = OutletRoomModel.builder()
                .id(UUID.randomUUID())
                .name("Room A")
                .outlet(outletModel)
                .build();

        OutletRoom domain = mapper.toDomainEntity(model);

        assertThat(domain.getOutletId()).isEqualTo(outletId.toString());
        assertThat(domain.getName()).isEqualTo("Room A");
    }

    @DisplayName("Should set OutletRoomModel.outlet to a stub OutletModel populated only with the id from OutletRoom.outletId")
    @Test
    void toModelEntity_buildsOutletStubFromOutletId() {
        UUID outletId = UUID.randomUUID();
        OutletRoom domain = OutletRoom.builder()
                .id(UUID.randomUUID().toString())
                .outletId(outletId.toString())
                .name("Room A")
                .build();

        OutletRoomModel model = mapper.toModelEntity(domain);

        assertThat(model.getOutlet()).isNotNull();
        assertThat(model.getOutlet().getId()).isEqualTo(outletId);
        assertThat(model.getOutlet().getName()).isNull();
        assertThat(model.getName()).isEqualTo("Room A");
    }

    @DisplayName("Should leave OutletRoomModel.outlet null when OutletRoom.outletId is null")
    @Test
    void toModelEntity_returnsNullOutletWhenOutletIdNull() {
        OutletRoom domain = OutletRoom.builder()
                .id(UUID.randomUUID().toString())
                .outletId(null)
                .build();

        OutletRoomModel model = mapper.toModelEntity(domain);

        assertThat(model.getOutlet()).isNull();
    }

    @DisplayName("Should preserve outletId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesOutletId() {
        UUID outletId = UUID.randomUUID();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        OutletRoomModel original = OutletRoomModel.builder()
                .id(UUID.randomUUID())
                .outlet(outletModel)
                .name("Room A")
                .build();

        OutletRoomModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getOutlet().getId()).isEqualTo(outletId);
        assertThat(roundTripped.getName()).isEqualTo("Room A");
    }

    @DisplayName("Should propagate type/url/passcode/description between domain and model in both directions")
    @Test
    void typeAndVirtualFields_propagateBothWays() {
        OutletRoomModel model = OutletRoomModel.builder()
                .id(UUID.randomUUID())
                .name("Virtual Studio")
                .type(OutletRoom.OUTLET_ROOM_TYPE.VIRTUAL)
                .url("https://meet.example.com/abc")
                .passcode("123456")
                .description("Lorem ipsum")
                .build();

        OutletRoom domain = mapper.toDomainEntity(model);
        assertThat(domain.getType()).isEqualTo(OutletRoom.OUTLET_ROOM_TYPE.VIRTUAL);
        assertThat(domain.getUrl()).isEqualTo("https://meet.example.com/abc");
        assertThat(domain.getPasscode()).isEqualTo("123456");
        assertThat(domain.getDescription()).isEqualTo("Lorem ipsum");

        OutletRoomModel back = mapper.toModelEntity(domain);
        assertThat(back.getType()).isEqualTo(OutletRoom.OUTLET_ROOM_TYPE.VIRTUAL);
        assertThat(back.getUrl()).isEqualTo("https://meet.example.com/abc");
        assertThat(back.getPasscode()).isEqualTo("123456");
        assertThat(back.getDescription()).isEqualTo("Lorem ipsum");
    }
}
