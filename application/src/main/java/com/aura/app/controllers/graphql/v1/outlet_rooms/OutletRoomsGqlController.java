package com.aura.app.controllers.graphql.v1.outlet_rooms;

import com.aura.app.codegen.types.CreateOutletRoomInput;
import com.aura.app.codegen.types.DeleteOutletRoomByIdInput;
import com.aura.app.codegen.types.OutletRoomResponse;
import com.aura.app.codegen.types.UpdateOutletRoomInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.OutletRoomMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.outlets.OutletRoom;
import com.aura.details.services.OutletRoomService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OutletRoomsGqlController {

    private final OutletRoomService outletRoomService;

    @PreAuthorize("hasPermission(#createOutletRoomInput.outletId, 'outlet', 'create_outlet_room')")
    @MutationMapping
    public OutletRoomResponse createOutletRoom(@Argument("createOutletRoomInput") CreateOutletRoomInput createOutletRoomInput,
                                               @ContextValue(name = "user") User user) {
        OutletRoom created = outletRoomService.createOutletRoom(OutletRoomMapper.INSTANCE.toDomainEntity(createOutletRoomInput));
        return OutletRoomMapper.INSTANCE.toDto(created);
    }

    @PreAuthorize("hasPermission(#updateOutletRoomInput.outletId, 'outlet', 'update_outlet_room')")
    @MutationMapping
    public OutletRoomResponse updateOutletRoom(@Argument("updateOutletRoomInput") UpdateOutletRoomInput updateOutletRoomInput,
                                               @ContextValue(name = "user") User user) {
        return Optional.ofNullable(outletRoomService.updateOutletRoom(OutletRoomMapper.INSTANCE.toDomainEntity(updateOutletRoomInput)))
                .map(OutletRoomMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("OutletRoom"));
    }

    @PreAuthorize("hasPermission(#deleteOutletRoomByIdInput.outletId, 'outlet', 'delete_outlet_room')")
    @MutationMapping
    public Boolean deleteOutletRoomById(@Argument("deleteOutletRoomByIdInput") DeleteOutletRoomByIdInput deleteOutletRoomByIdInput,
                                        @ContextValue(name = "user") User user) {
        if (outletRoomService.findById(deleteOutletRoomByIdInput.getId()) == null) {
            throw new ResourceNotFoundBusinessException("OutletRoom");
        }
        outletRoomService.deleteById(deleteOutletRoomByIdInput.getId());
        return true;
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<OutletRoomResponse> getAllOutletRoomsByIds(@Argument("institutionId") String institutionId,
                                                           @Argument("ids") List<String> ids,
                                                           @ContextValue(name = "user") User user) {
        return outletRoomService.findAllByIds(ids.stream().map(UUID::fromString).toList()).stream()
                .filter(room -> institutionId.equals(room.getTenantId()))
                .map(OutletRoomMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<OutletRoomResponse> getAllOutletRoomsInInstitution(@Argument("institutionId") String institutionId,
                                                                   @ContextValue(name = "user") User user) {
        return outletRoomService.findAllByTenantId(institutionId).stream()
                .map(OutletRoomMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#outletId, 'outlet', 'view')")
    @QueryMapping
    public List<OutletRoomResponse> getAllOutletRoomsInOutlet(@Argument("institutionId") String institutionId,
                                                              @Argument("outletId") String outletId,
                                                              @ContextValue(name = "user") User user) {
        return outletRoomService.findAllByOutletId(outletId).stream()
                .filter(room -> institutionId.equals(room.getTenantId()))
                .map(OutletRoomMapper.INSTANCE::toDto)
                .toList();
    }
}
