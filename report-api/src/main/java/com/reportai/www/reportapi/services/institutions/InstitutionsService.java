package com.reportai.www.reportapi.services.institutions;

import com.reportai.www.reportapi.contexts.requests.TenantContext;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.mappers.InstitutionMappers;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.schools.SchoolsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import com.reportai.www.reportapi.services.topics.TopicsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InstitutionsService implements ISimpleRead<Institution> {

    private final InstitutionRepository institutionRepository;
    private final List<String> ALL_INSTITUTION_ROLES = List.of("institution-admin", "student-report-mobile", "educator-report-mobile");
    private final ClientResource clientResource;
    private final SchoolsService schoolsService;
    private final LevelsService levelsService;
    private final SubjectsService subjectsService;
    private final TopicsService topicsService;

    @Autowired
    public InstitutionsService(InstitutionRepository institutionRepository, SchoolsService schoolsService, @Lazy LevelsService levelsService, ClientResource clientResource, SubjectsService subjectsService, TopicsService topicsService) {
        this.institutionRepository = institutionRepository;
        this.schoolsService = schoolsService;
        this.levelsService = levelsService;
        this.clientResource = clientResource;
        this.subjectsService = subjectsService;
        this.topicsService = topicsService;
    }

    @Transactional
    public Institution update(@NonNull Institution updatedInstitution) {
        Institution currentInstitution = getInstitutionFromContext();
        Institution institution = InstitutionMappers.layover(currentInstitution, updatedInstitution); // TODO: change to model mapper
        return institutionRepository.save(institution);
    }

    /**
     * gets current institution details from request context
     *
     * @return
     */
    @Transactional
    public Institution getInstitutionFromContext() {
        return institutionRepository.findById(UUID.fromString(TenantContext.getTenantId())).orElseThrow(HttpInstitutionNotFoundException::new);
    }


    /**
     * Creates institution as well as all roles for the institution
     *
     * @param requestedInstitution
     * @return
     */
    @Transactional
    public Institution create(@NonNull Institution requestedInstitution) {
        // temporarily save the auraAdminTenantId which is "*"
        String tentativeTenantId = TenantContext.getTenantId();

        // set newly generated tenant id to create institution
        requestedInstitution.setId(UUID.fromString(tentativeTenantId));
        try {
            Institution createdInstitution = institutionRepository.saveAndFlush(requestedInstitution);
            schoolsService.createDefaultSchools(createdInstitution.getId());
            levelsService.createDefaultLevels(createdInstitution.getId());
            subjectsService.createDefaultSubjects(createdInstitution.getId());
            topicsService.createDefaultTopics(createdInstitution.getId());

            // create all institution roles
            allTenantAwareInstitutionRoles(createdInstitution.getId().toString(), createdInstitution.getName()).forEach(cr -> {
                log.info("creating client role {} for institution {}", cr.getName(), createdInstitution.getName());
                clientResource.roles().create(cr);
            });
            return createdInstitution;

        } finally {
            // return the context back to the AuraAdminTenant context
            TenantContext.clear();
        }
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
        return institutionRepository;
    }
}
