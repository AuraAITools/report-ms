package com.aura.app.utils;

import com.aura.app.codegen.types.SortInput;
import com.aura.app.dto.PageParametersDto;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.PaginationCriteria;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.query.ScrollSubrange;


import static com.aura.app.utils.SortUtil.resolveDirection;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public class PaginationUtils {
    private PaginationUtils() {
    }

    public static PaginationCriteria.PaginationDirection mapDirection(
            ScrollPosition.Direction direction) {
        return direction == ScrollPosition.Direction.FORWARD ? PaginationCriteria.PaginationDirection.FORWARD : PaginationCriteria.PaginationDirection.BACKWARD;
    }

    public static <T, R> Connection<R> buildConnection(PagedResult<T> pagedResult, List<SortCriteria> sortCriteriasList, Function<T, R> mapFn, Function<Map<String, Object>, String> cursorProvider) {

        List<T> edgeContentList = pagedResult.getItems();
        List<Edge<R>> edges = new ArrayList<>();
        ConnectionCursor startCursor = null;
        ConnectionCursor endCursor = null;
        for (int i = 0; i < edgeContentList.size(); i++) {
            T edgeContent = edgeContentList.get(i);
            Map<String, Object> keyset = extractKeysetFromEntity(edgeContent, sortCriteriasList);
            R destination = mapFn.apply(edgeContent);
            ConnectionCursor cursor = new DefaultConnectionCursor(cursorProvider.apply(keyset));
            if (i == 0) startCursor = cursor;
            if (i == edgeContentList.size() - 1) endCursor = cursor;
            edges.add(new DefaultEdge<>(destination, cursor));
        }

        PageInfo pageInfo = new DefaultPageInfo(startCursor, endCursor, pagedResult.getPageInfo().hasPreviousPage(), pagedResult.getPageInfo().hasNextPage());

        return new DefaultConnection<>(edges, pageInfo);
    }


    public static <T> Map<String, Object> extractKeysetFromEntity(T entity, List<SortCriteria> sortCriteria) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        @SuppressWarnings("unchecked")
        Map<String, Object> entityMap = objectMapper.convertValue(entity, Map.class);

        Map<String, Object> keyset = new HashMap<>();

        for (SortCriteria criteria : sortCriteria) {
            String field = criteria.getField();
            if (entityMap.containsKey(field)) {
                keyset.put(field, entityMap.get(field));
            }
        }

        // Ensure ID is always present
        if (!keyset.containsKey("id") && entityMap.containsKey("id")) {
            keyset.put("id", entityMap.get("id"));
        }

        return keyset;
    }

    public static <T> PageParametersDto getPageParametersDto(T searchInputs, SortInput sortInput, ScrollSubrange scrollSubrange, Function<T, Map<String, Object>> searchFilterMapperFn) {
        ScrollPosition scrollPosition = scrollSubrange.position().orElse(ScrollPosition.keyset());
        Optional<T> optionalSearchInputs = ofNullable(searchInputs);
        Optional<SortInput> optionalSortInput = ofNullable(sortInput);

        int limit = scrollSubrange.count().orElse(10);

        // only keyset pagination is supported
        if (!(scrollPosition instanceof KeysetScrollPosition keysetScrollPosition))
            throw new UnsupportedOperationException("Only Keyset Cursor Pagination is supported");

        // if search input is present, search
        Map<String, Object> searchFilters = optionalSearchInputs.map(searchFilterMapperFn).orElse(Collections.emptyMap());
        SearchCriteria searchCriteria = new SearchCriteria(searchFilters);

        // if sort input is present, sort
        List<SortCriteria> sortCriteriasList = optionalSortInput
                .map(input -> new SortCriteria(input.getField(), resolveDirection(input.getDirection())))
                .map(SortUtil::defaultSortsWith).orElse(emptyList());

        KeysetPaginationCriteria keysetPaginationCriteria = new KeysetPaginationCriteria(limit, PaginationUtils.mapDirection(keysetScrollPosition.getDirection()), keysetScrollPosition.getKeys());
        return new PageParametersDto(searchCriteria, sortCriteriasList, keysetPaginationCriteria);
    }
}
