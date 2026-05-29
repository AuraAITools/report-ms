package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.entities.outlets.OutletRoom.OUTLET_ROOM_TYPE;
import com.aura.core.repositories.OutletRoomRepository;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Update outlet-room use case")
@ExtendWith(MockitoExtension.class)
class UpdateOutletRoomUseCaseImplTest {

    @Mock
    private OutletRoomRepository outletRoomRepository;
    @InjectMocks
    private UpdateOutletRoomUseCaseImpl useCase;

    @DisplayName("Should overwrite editable fields, preserve id/tenantId/outletId/audit, and save")
    @Test
    void updateOutletRoom_overwritesEditableFieldsAndSaves() {
        OutletRoom existing = OutletRoom.builder()
                .id("room-1")
                .outletId("outlet-1")
                .tenantId("tenant-1")
                .createdBy("creator")
                .name("Old Name")
                .size(10)
                .details("Old details")
                .fileUrl("old-file-url")
                .type(OUTLET_ROOM_TYPE.PHYSICAL)
                .url(null)
                .passcode(null)
                .description("Old desc")
                .build();
        OutletRoom input = OutletRoom.builder()
                .id("room-1")
                .tenantId("tenant-1")
                .name("New Name")
                .size(20)
                .details("New details")
                .fileUrl("new-file-url")
                .type(OUTLET_ROOM_TYPE.VIRTUAL)
                .url("https://meet.example.com/abc")
                .passcode("123456")
                .description("New desc")
                .build();
        when(outletRoomRepository.findById("room-1")).thenReturn(existing);
        when(outletRoomRepository.save(any(OutletRoom.class))).thenAnswer(inv -> inv.getArgument(0));

        OutletRoom result = useCase.updateOutletRoom(input);

        ArgumentCaptor<OutletRoom> savedCaptor = ArgumentCaptor.forClass(OutletRoom.class);
        verify(outletRoomRepository).save(savedCaptor.capture());
        OutletRoom saved = savedCaptor.getValue();

        assertThat(saved.getId()).isEqualTo("room-1");
        assertThat(saved.getOutletId()).isEqualTo("outlet-1");
        assertThat(saved.getTenantId()).isEqualTo("tenant-1");
        assertThat(saved.getCreatedBy()).isEqualTo("creator");
        assertThat(saved.getName()).isEqualTo("New Name");
        assertThat(saved.getSize()).isEqualTo(20);
        assertThat(saved.getDetails()).isEqualTo("New details");
        assertThat(saved.getFileUrl()).isEqualTo("new-file-url");
        assertThat(saved.getType()).isEqualTo(OUTLET_ROOM_TYPE.VIRTUAL);
        assertThat(saved.getUrl()).isEqualTo("https://meet.example.com/abc");
        assertThat(saved.getPasscode()).isEqualTo("123456");
        assertThat(saved.getDescription()).isEqualTo("New desc");
        assertThat(result).isSameAs(saved);
    }

    @DisplayName("Should return null and skip save when outlet room does not exist")
    @Test
    void updateOutletRoom_returnsNullWhenNotFound() {
        OutletRoom input = OutletRoom.builder().id("missing").tenantId("tenant-1").build();
        when(outletRoomRepository.findById("missing")).thenReturn(null);

        assertThat(useCase.updateOutletRoom(input)).isNull();
        verify(outletRoomRepository).findById("missing");
        verifyNoMoreInteractions(outletRoomRepository);
    }

    @DisplayName("Should return null and skip save when input tenantId does not match the loaded room's tenant")
    @Test
    void updateOutletRoom_returnsNullWhenTenantMismatch() {
        OutletRoom existing = OutletRoom.builder().id("room-1").tenantId("tenant-A").build();
        OutletRoom input = OutletRoom.builder().id("room-1").tenantId("tenant-B").build();
        when(outletRoomRepository.findById("room-1")).thenReturn(existing);

        assertThat(useCase.updateOutletRoom(input)).isNull();
        verify(outletRoomRepository).findById("room-1");
        verifyNoMoreInteractions(outletRoomRepository);
    }
}
