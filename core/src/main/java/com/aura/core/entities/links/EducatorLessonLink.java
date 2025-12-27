package com.aura.core.entities.links;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.lessons.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EducatorLessonLink {

    private Educator educator;
    private Lesson lesson;

}
