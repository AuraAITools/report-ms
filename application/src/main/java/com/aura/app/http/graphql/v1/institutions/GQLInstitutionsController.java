package com.aura.app.http.graphql.v1.institutions;

import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.http.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.app.http.graphql.v1.institutions.mappers.InstitutionMapper;
import com.aura.app.http.rest.v1.institutions.mappers.UserMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.app.utils.SortUtil;
import com.aura.core.auth.AuthorizationValidator;
import com.aura.core.auth.RBACAuthorizationStrategy;
import com.aura.core.entities.accounts.TenantAwareRole;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.institutions.services.OnboardingInstitutionService;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;


import static com.aura.app.utils.SortUtil.resolveDirection;

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
    private final InstitutionRepository institutionRepository;
    private final CursorStrategy<ScrollPosition> cursorStrategy;
    private final ObjectMapper objectMapper;

    public GQLInstitutionsController(AuthorizationValidator roleValidator, OnboardingInstitutionService onboardingInstitutionService, InstitutionRepository institutionRepository, CursorStrategy<ScrollPosition> cursorStrategy) {
        this.roleValidator = roleValidator;
        this.onboardingInstitutionService = onboardingInstitutionService;
        this.institutionRepository = institutionRepository;
        this.cursorStrategy = cursorStrategy;
        this.objectMapper = new ObjectMapper();
    }

    private static Map<String, Object> convertToMap(Object object, ObjectMapper mapper) {
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

    @QueryMapping(name = "institution")
    public Connection<InstitutionResponse> institution(
            @Argument(name = "search") Optional<InstitutionSearchInput> searchInputs,
            @Argument(name = "sort") Optional<SortInput> sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange) {
        // use keyset cursor pagination
        ScrollPosition scrollPosition = scrollSubrange.position().orElse(ScrollPosition.keyset());

        // only keyset pagination is supported
        if (!(scrollPosition instanceof KeysetScrollPosition keysetScrollPosition))
            throw new UnsupportedOperationException("Only Keyset Cursor Pagination is supported");

        // if search input is present, search
        Map<String, Object> searchFilters = searchInputs.map(si -> convertToMap(si, objectMapper)).orElse(Collections.emptyMap());
        SearchCriteria searchCriteria = new SearchCriteria(searchFilters);

        // if sort input is present, sort
        Optional<SortCriteria> sortCriteria = sortInput.map(input -> new SortCriteria(input.getField(), resolveDirection(input.getDirection())));
        List<SortCriteria> sortCriteriasList = sortCriteria.map(SortUtil::defaultSortsWith).orElse(Collections.emptyList());
        int limit = scrollSubrange.count().orElse(10);

        KeysetPaginationCriteria keysetPaginationCriteria = new KeysetPaginationCriteria(limit, PaginationUtils.mapDirection(keysetScrollPosition.getDirection()), keysetScrollPosition.getKeys());

        PagedResult<Institution> pagedResult = institutionRepository.findAllBy(keysetPaginationCriteria, sortCriteriasList, searchCriteria);

        return PaginationUtils.buildConnection(pagedResult, sortCriteriasList, InstitutionMapper.INSTANCE::from, (keyset) -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }
}
