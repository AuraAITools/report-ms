package com.aura.app.dto;

import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;

public record PageParametersDto(SearchCriteria searchCriteria, List<SortCriteria> sortCriteriasList,
                                KeysetPaginationCriteria keysetPaginationCriteria) {
}
