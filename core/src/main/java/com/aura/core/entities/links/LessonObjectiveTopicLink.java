package com.aura.core.entities.links;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.entities.topics.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonObjectiveTopicLink {

    private Topic topic;

    private LessonObjective lessonObjective;

}
