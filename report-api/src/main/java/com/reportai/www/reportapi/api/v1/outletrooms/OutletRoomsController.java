package com.reportai.www.reportapi.api.v1.outletrooms;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.outletrooms.requests.CreateOutletRoomRequestDTO;
import com.reportai.www.reportapi.api.v1.outletrooms.response.OutletRoomResponseDTO;
import com.reportai.www.reportapi.entities.OutletRoom;
import com.reportai.www.reportapi.services.outlets.OutletRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Outlet Room APIs", description = "APIs for managing a Outlet Room resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class OutletRoomsController {

    private final OutletRoomService outletRoomService;
    private final ModelMapper modelMapper;

    @Autowired
    public OutletRoomsController(OutletRoomService outletRoomService, ModelMapper modelMapper) {
        this.outletRoomService = outletRoomService;
        this.modelMapper = modelMapper;
//        modelMapper.createTypeMap(OutletRoom.class, OutletRoomResponseDTO.class) // TODO: refactopr to another file
//                .addMapping(OutletRoom::getId, OutletRoomResponseDTO::setId);
    }


    @Operation(summary = "create a outlet room for a outlet", description = "create an outlet room for a outlet")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/outlet-rooms")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::outlet-rooms:create'")
    public ResponseEntity<OutletRoomResponseDTO> createOutletForInstitution(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @RequestBody @Valid CreateOutletRoomRequestDTO createOutletRequestDTO) {
        OutletRoom outletRoom = new OutletRoom();
        modelMapper.map(createOutletRequestDTO, outletRoom);
        OutletRoom createdOutletRoom = outletRoomService.create(outletId, outletRoom);
        return new ResponseEntity<>(modelMapper.map(createdOutletRoom, OutletRoomResponseDTO.class), HttpStatus.CREATED);
    }

    @Operation(summary = "get all outlet rooms for a institution", description = "get all outletrooms for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/outlet-rooms")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::outlet-rooms:read'")
    public ResponseEntity<List<OutletRoomResponseDTO>> getAllOutletRoomsFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<OutletRoom> outletRooms = outletRoomService.getAllInOutlet(outletId);
        List<OutletRoomResponseDTO> outletRoomResponseDTOS = outletRooms
                .stream()
                .map(outletRoom -> modelMapper.map(outletRoom, OutletRoomResponseDTO.class))
                .toList();
        return new ResponseEntity<>(outletRoomResponseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "get outlet room from outlet", description = "get outletroom from outlet")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/outlet-rooms/{outlet_room_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::outlet-rooms:read'")
    public ResponseEntity<OutletRoomResponseDTO> getOutletRoomFromOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "outlet_room_id") UUID outletRoomId) {
        OutletRoom outletRoom = outletRoomService.findById(outletRoomId);
        return new ResponseEntity<>(modelMapper.map(outletRoom, OutletRoomResponseDTO.class), HttpStatus.OK);
    }
}
