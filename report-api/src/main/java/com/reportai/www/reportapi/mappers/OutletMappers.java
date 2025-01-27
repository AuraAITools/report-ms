package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.outlets.requests.CreateOutletDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDto;
import com.reportai.www.reportapi.entities.Outlet;

public class OutletMappers {
    public static Outlet convert(CreateOutletDTO createOutletDTO, String tenantId) {
        return Outlet
                .builder()
                .address(createOutletDTO.getAddress())
                .name(createOutletDTO.getName())
                .contactNumber(createOutletDTO.getContactNumber())
                .postalCode(createOutletDTO.getPostalCode())
                .email(createOutletDTO.getEmail())
                .tenantId(tenantId)
                .build();
    }

    public static OutletResponseDto convert(Outlet outlet) {
        return OutletResponseDto
                .builder()
                .id(outlet.getId().toString())
                .address(outlet.getAddress())
                .contactNumber(outlet.getContactNumber())
                .postalCode(outlet.getPostalCode())
                .name(outlet.getName())
                .build();
    }
}
