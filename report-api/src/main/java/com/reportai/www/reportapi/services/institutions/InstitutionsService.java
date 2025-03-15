package com.reportai.www.reportapi.services.institutions;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.mappers.InstitutionMappers;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InstitutionsService implements BaseServiceTemplate<Institution, UUID> {

    private final InstitutionRepository institutionRepository;
    private final List<String> ALL_INSTITUTION_ROLES = List.of("institution-admin", "student-report-mobile", "educator-report-mobile");
    private final ClientResource clientResource;


    public InstitutionsService(InstitutionRepository institutionRepository, ClientResource clientResource) {
        this.institutionRepository = institutionRepository;
        this.clientResource = clientResource;
    }


    @Transactional
    public Institution updateInstitution(@NonNull UUID id, @NonNull Institution updates) {
        Institution existingInstitution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        Institution institution = InstitutionMappers.layover(existingInstitution, updates);
        return institutionRepository.save(institution);
    }

    /**
     * Creates institution as well as all roles for the institution
     *
     * @param requestedInstitution
     * @return
     */
    @Transactional
    public Institution createInstitution(@NonNull Institution requestedInstitution) {
        Institution createdInstitution = institutionRepository.save(requestedInstitution);
        // create all institution roles
        allTenantAwareInstitutionRoles(createdInstitution.getId().toString(), createdInstitution.getName()).forEach(cr -> {
            log.info("creating client role {} for institution {}", cr.getName(), createdInstitution.getName());
            clientResource.roles().create(cr);
        });

        // create institution
        return createdInstitution;
    }

    private List<RoleRepresentation> allTenantAwareInstitutionRoles(@NonNull String prefix, @NonNull String institutionName) {
        return ALL_INSTITUTION_ROLES.stream().map(role -> {
            RoleRepresentation clientRole = new RoleRepresentation();
            clientRole.setClientRole(true);
            clientRole.setDescription(institutionName);
            clientRole.setName(String.format("%s_%s", prefix, role));
            return clientRole;
        }).toList();

    }

    @Override
    public JpaRepository<Institution, UUID> getRepository() {
        return this.institutionRepository;
    }
}
