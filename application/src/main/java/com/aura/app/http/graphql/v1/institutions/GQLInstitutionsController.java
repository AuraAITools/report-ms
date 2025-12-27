package com.aura.app.http.graphql.v1.institutions;

import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.http.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.app.http.graphql.v1.institutions.mappers.InstitutionMapper;
import com.aura.app.http.rest.v1.institutions.mappers.UserMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.AuthorizationValidator;
import com.aura.core.auth.RBACAuthorizationStrategy;
import com.aura.core.commons.QueryService;
import com.aura.core.entities.accounts.TenantAwareRole;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.institutions.services.OnboardingInstitutionService;
import com.aura.core.utils.PagedResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;


import static com.aura.app.utils.PaginationUtils.getPageParametersDto;

@Slf4j
@Controller
public class GQLInstitutionsController {
    private static final Set<TenantAwareRole> ALLOWED_ROLES = Set.of(TenantAwareRole
            .builder()
            .name("aura-admin")
            .roleType(TenantAwareRole.ROLE_TYPE.TECHNICAL_USER)
            .build());
    public final AuthorizationValidator roleValidator;
    private final OnboardingInstitutionService onboardingInstitutionService;
    private final QueryService<Institution> institutionQueryService;
    private final CursorStrategy<ScrollPosition> cursorStrategy;
    private final ObjectMapper objectMapper;

    public GQLInstitutionsController(AuthorizationValidator roleValidator, OnboardingInstitutionService onboardingInstitutionService, QueryService<Institution> institutionQueryService, CursorStrategy<ScrollPosition> cursorStrategy) {
        this.roleValidator = roleValidator;
        this.onboardingInstitutionService = onboardingInstitutionService;
        this.institutionQueryService = institutionQueryService;
        this.cursorStrategy = cursorStrategy;
        this.objectMapper = new ObjectMapper();
    }

    private static Map<String, Object> mapToSearchFilters(Object object, ObjectMapper mapper) {
        return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    @MutationMapping(name = DgsConstants.MUTATION.CreateInstitution)
    public InstitutionResponse createInstitution(@Argument(value = DgsConstants.MUTATION.CREATEINSTITUTION_INPUT_ARGUMENT.CreateInstitutionInput) CreateInstitutionInput createInstitutionInput) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Jwt jwt)) throw new AccessDeniedException("Invalid principal type");
        roleValidator.validateAuthorization(new RBACAuthorizationStrategy(ALLOWED_ROLES, UserMapper.from(jwt)));
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingInstitutionService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }

    @QueryMapping(name = DgsConstants.QUERY.GetInstitutionByPage)
    public Connection<InstitutionResponse> getInstitutionByPage(
            @Argument(name = "search") InstitutionSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange) {
        // use keyset cursor pagination
        PageParametersDto pageParameters = getPageParametersDto(searchInputs, sortInput, scrollSubrange, (searchInputParameters) -> mapToSearchFilters(searchInputParameters, objectMapper));

        PagedResult<Institution> pagedResult = institutionQueryService.queryPageBy(pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());

        return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), InstitutionMapper.INSTANCE::from, (keyset) -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }
}
