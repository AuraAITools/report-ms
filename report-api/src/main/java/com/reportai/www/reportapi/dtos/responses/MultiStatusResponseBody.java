package com.reportai.www.reportapi.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Slf4j
public class MultiStatusResponseBody<T> {

    private List<T> results;

    public MultiStatusResponseBody() {
        this.results = new ArrayList<>();
    }


    public MultiStatusResponseBody<T> add(Supplier<T> individualResults) {
        results.add(individualResults.get());
        return this;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public List<T> getResults() {
        return this.results;
    }

    public MultiStatusResponseBody<T> build() {
        return this;
    }
}
