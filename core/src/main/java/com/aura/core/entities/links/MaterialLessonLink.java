package com.aura.core.entities.links;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.materials.Material;
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
public class MaterialLessonLink {

    private Material material;

    private Lesson lesson;

    private LessonPlan lessonPlan;

}
