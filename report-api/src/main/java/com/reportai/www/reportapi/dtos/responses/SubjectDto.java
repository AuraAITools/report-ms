package com.reportai.www.reportapi.dtos.responses;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private UUID id;
    private String name;
    private List<LessonDto> lessons;
}
