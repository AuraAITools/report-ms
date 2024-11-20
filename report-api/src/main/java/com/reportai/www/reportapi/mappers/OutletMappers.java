package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.dtos.requests.CreateOutletDTO;
import com.reportai.www.reportapi.dtos.responses.OutletResponseDto;
import com.reportai.www.reportapi.entities.Outlet;

public class OutletMappers {
    public static Outlet convert(CreateOutletDTO createOutletDTO) {
        return Outlet
                .builder()
                .address(createOutletDTO.getAddress())
                .name(createOutletDTO.getName())
                .contactNumber(createOutletDTO.getContactNumber())
                .postalCode(createOutletDTO.getPostalCode())
                .build();
    }

    public static OutletResponseDto convert(Outlet outlet) {
        return OutletResponseDto
                .builder()
                .address(outlet.getAddress())
                .contactNumber(outlet.getContactNumber())
                .postalCode(outlet.getPostalCode())
                .name(outlet.getName())
                .build();
    }
}
