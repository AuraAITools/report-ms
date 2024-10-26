package com.reportai.www.reportapi.dtos.responses;

import java.time.LocalDateTime;
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
public class LessonDto {
    private UUID id;
    private String title;
    private LocalDateTime lessonDate;
    private long duration;
}
