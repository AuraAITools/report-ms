package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.outlets.requests.CreateOutletRequestDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.ExpandedOutletsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
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
                .build();
    }

    public static OutletResponseDTO convert(Outlet outlet) {
        return OutletResponseDTO
                .builder()
                .id(outlet.getId().toString())
                .address(outlet.getAddress())
                .contactNumber(outlet.getContactNumber())
                .postalCode(outlet.getPostalCode())
                .name(outlet.getName())
                .email(outlet.getEmail())
                .description(outlet.getDescription())
                .build();
    }

    public static ExpandedOutletsResponseDTO convertExpanded(Outlet outlet) {
        return ExpandedOutletsResponseDTO
                .builder()
                .id(outlet.getId().toString())
                .address(outlet.getAddress())
                .contactNumber(outlet.getContactNumber())
                .postalCode(outlet.getPostalCode())
                .name(outlet.getName())
                .email(outlet.getEmail())
                .description(outlet.getDescription())
                .educators(outlet.getOutletEducatorAttachments() == null ? null :
                        outlet.getOutletEducatorAttachments()
                                .stream()
                                .map(outletEducatorAttachment -> EducatorMappers.convert(outletEducatorAttachment.getEducator()))
                                .toList())
                .students(outlet.getStudentOutletRegistrations() == null ? null :
                        outlet
                                .getStudentOutletRegistrations()
                                .stream()
                                .map(studentOutletRegistration -> StudentMappers.convert(studentOutletRegistration.getStudent()))
                                .toList())
                .courses(outlet.getCourses().stream().map(CourseMappers::convert).toList())
                .build();
    }
}
