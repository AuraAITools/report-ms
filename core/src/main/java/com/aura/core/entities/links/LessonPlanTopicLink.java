package com.aura.core.entities.links;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.topics.Topic;
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
public class LessonPlanTopicLink {
    private LessonPlan lessonPlan;

    private Topic topic;

}
