package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.outlets.requests.CreateOutletRequestDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.CreateOutletResponseDto;
import com.reportai.www.reportapi.entities.Outlet;

public class OutletMappers {
    public static Outlet convert(CreateOutletRequestDTO createOutletRequestDTO, String tenantId) {
        return Outlet
                .builder()
                .address(createOutletRequestDTO.getAddress())
                .name(createOutletRequestDTO.getName())
                .contactNumber(createOutletRequestDTO.getContactNumber())
                .postalCode(createOutletRequestDTO.getPostalCode())
                .email(createOutletRequestDTO.getEmail())
                .description(createOutletRequestDTO.getDescription())
                .tenantId(tenantId)
                .build();
    }

    public static CreateOutletResponseDto convert(Outlet outlet) {
        return CreateOutletResponseDto
                .builder()
                .id(outlet.getId().toString())
                .address(outlet.getAddress())
                .contactNumber(outlet.getContactNumber())
                .postalCode(outlet.getPostalCode())
                .name(outlet.getName())
                .description(outlet.getDescription())
//                .educators(outlet.getEducators().stream().map(EducatorMappers::convert).toList())
//                .students(outlet.getStudents().stream().map(StudentMappers::convert).toList())
//                .courses(outlet.getCourses().stream().map(CourseMappers::convert).toList())
                .build();
    }
}
