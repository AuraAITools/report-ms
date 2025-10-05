package com.aura.app.utils;

import com.aura.core.utils.PagedResult;
import com.aura.core.utils.PaginationCriteria;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.data.domain.ScrollPosition;

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
}
