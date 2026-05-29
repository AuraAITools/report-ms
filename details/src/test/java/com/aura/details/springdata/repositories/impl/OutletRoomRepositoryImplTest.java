package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRoomRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("OutletRoom repository impl")
@ExtendWith(MockitoExtension.class)
class OutletRoomRepositoryImplTest {

    @Mock
    private JpaOutletRoomRepository jpaOutletRoomRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private OutletRoomRepositoryImpl repository;

    @DisplayName("Should attach a managed OutletModel proxy via EntityManager.getReference before persisting")
    @Test
    void save_attachesOutletRefViaEntityManager() {
        UUID outletId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        OutletModel outletProxy = OutletModel.builder().id(outletId).build();
        when(entityManager.getReference(OutletModel.class, outletId)).thenReturn(outletProxy);

        OutletRoom input = OutletRoom.builder()
                .id(roomId.toString())
                .outletId(outletId.toString())
                .name("Room A")
                .size(12)
                .type(OutletRoom.OUTLET_ROOM_TYPE.PHYSICAL)
                .build();
        OutletRoomModel persisted = OutletRoomModel.builder()
                .id(roomId)
                .outlet(outletProxy)
                .name("Room A")
                .size(12)
                .type(OutletRoom.OUTLET_ROOM_TYPE.PHYSICAL)
                .build();
        when(jpaOutletRoomRepository.save(any(OutletRoomModel.class))).thenReturn(persisted);

        OutletRoom result = repository.save(input);

        ArgumentCaptor<OutletRoomModel> captor = ArgumentCaptor.forClass(OutletRoomModel.class);
        verify(jpaOutletRoomRepository).save(captor.capture());
        assertThat(captor.getValue().getOutlet()).isSameAs(outletProxy);
        assertThat(result.getOutletId()).isEqualTo(outletId.toString());
        assertThat(result.getType()).isEqualTo(OutletRoom.OUTLET_ROOM_TYPE.PHYSICAL);
    }

    @DisplayName("Should return mapped room when JPA finds it")
    @Test
    void findById_returnsRoomWhenPresent() {
        UUID id = UUID.randomUUID();
        OutletRoomModel model = OutletRoomModel.builder().id(id).name("X").build();
        when(jpaOutletRoomRepository.findById(id)).thenReturn(Optional.of(model));

        OutletRoom result = repository.findById(id.toString());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("X");
    }

    @DisplayName("Should return null when JPA does not find the room")
    @Test
    void findById_returnsNullWhenAbsent() {
        UUID id = UUID.randomUUID();
        when(jpaOutletRoomRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(repository.findById(id.toString())).isNull();
    }

    @DisplayName("Should short-circuit findAllByIds when the input list is empty")
    @Test
    void findAllByIds_returnsEmptyWithoutHittingDb() {
        assertThat(repository.findAllByIds(List.of())).isEmpty();
        verifyNoInteractions(jpaOutletRoomRepository);
    }

    @DisplayName("Should map the JPA results for findAllByIds")
    @Test
    void findAllByIds_mapsResults() {
        UUID id = UUID.randomUUID();
        OutletRoomModel model = OutletRoomModel.builder().id(id).name("X").build();
        when(jpaOutletRoomRepository.findAllById(List.of(id))).thenReturn(List.of(model));

        List<OutletRoom> rooms = repository.findAllByIds(List.of(id));

        assertThat(rooms).hasSize(1);
        assertThat(rooms.get(0).getName()).isEqualTo("X");
    }

    @DisplayName("Should map the JPA results for findAllByOutletId")
    @Test
    void findAllByOutletId_mapsResults() {
        UUID outletId = UUID.randomUUID();
        OutletRoomModel m1 = OutletRoomModel.builder().id(UUID.randomUUID()).name("A").build();
        when(jpaOutletRoomRepository.findAllByOutletId(outletId)).thenReturn(List.of(m1));

        List<OutletRoom> rooms = repository.findAllByOutletId(outletId.toString());

        assertThat(rooms).extracting(OutletRoom::getName).containsExactly("A");
    }

    @DisplayName("Should map the JPA results for findAllByTenantId")
    @Test
    void findAllByTenantId_mapsResults() {
        OutletRoomModel m1 = OutletRoomModel.builder().id(UUID.randomUUID()).name("A").build();
        OutletRoomModel m2 = OutletRoomModel.builder().id(UUID.randomUUID()).name("B").build();
        when(jpaOutletRoomRepository.findAllByTenantId("tenant-1")).thenReturn(List.of(m1, m2));

        List<OutletRoom> rooms = repository.findAllByTenantId("tenant-1");

        assertThat(rooms).extracting(OutletRoom::getName).containsExactly("A", "B");
    }

    @DisplayName("Should delegate deleteById to JPA with parsed UUID")
    @Test
    void deleteById_delegates() {
        UUID id = UUID.randomUUID();

        repository.deleteById(id.toString());

        verify(jpaOutletRoomRepository).deleteById(id);
    }
}
