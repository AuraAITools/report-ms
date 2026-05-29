package com.aura.app.controllers.graphql.v1.schools;

import com.aura.app.codegen.types.CreateSchoolInput;
import com.aura.app.codegen.types.DeleteSchoolInInstitutionInput;
import com.aura.app.codegen.types.SchoolResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.SchoolMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.schools.School;
import com.aura.details.services.SchoolService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESCHOOLININSTITUTION_INPUT_ARGUMENT.CreateSchoolInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateSchoolInInstitution;
import static com.aura.app.codegen.DgsConstants.MUTATION.DELETESCHOOLININSTITUTION_INPUT_ARGUMENT.DeleteSchoolInInstitutionInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.DeleteSchoolInInstitution;

@Controller
@RequiredArgsConstructor
public class SchoolsGqlController {

    private final SchoolService schoolService;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<SchoolResponse> getSchoolsInInstitution(@Argument("institutionId") String institutionId,
                                                        @ContextValue(name = "user") User user) {
        return schoolService.findAllByTenantId(institutionId).stream()
                .map(SchoolMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#createSchoolInput.institutionId, 'institution', 'create_school')")
    @MutationMapping(name = CreateSchoolInInstitution)
    public SchoolResponse createSchoolInInstitution(@Argument(name = CreateSchoolInput) CreateSchoolInput createSchoolInput,
                                                    @ContextValue(name = "user") User user) {
        School created = schoolService.createSchool(SchoolMapper.INSTANCE.toDomain(createSchoolInput));
        return SchoolMapper.INSTANCE.toDto(created);
    }

    @PreAuthorize("hasPermission(#deleteSchoolInInstitutionInput.institutionId, 'institution', 'delete_school')")
    @MutationMapping(name = DeleteSchoolInInstitution)
    public Boolean deleteSchoolInInstitution(@Argument(name = DeleteSchoolInInstitutionInput) DeleteSchoolInInstitutionInput deleteSchoolInInstitutionInput,
                                             @ContextValue(name = "user") User user) {
        if (schoolService.findById(deleteSchoolInInstitutionInput.getSchoolId()) == null) {
            throw new ResourceNotFoundBusinessException("School");
        }
        schoolService.deleteById(deleteSchoolInInstitutionInput.getSchoolId());
        return true;
    }
}
