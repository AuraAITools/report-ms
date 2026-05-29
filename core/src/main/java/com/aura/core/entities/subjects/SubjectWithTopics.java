package com.aura.core.entities.subjects;

import com.aura.core.entities.topics.Topic;
import java.util.List;

public record SubjectWithTopics(Subject subject, List<Topic> topics) {
}
