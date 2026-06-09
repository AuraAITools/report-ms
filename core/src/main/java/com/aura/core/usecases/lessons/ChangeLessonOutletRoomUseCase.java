package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import org.jspecify.annotations.Nullable;

public interface ChangeLessonOutletRoomUseCase {
    Lesson changeLessonOutletRoom(String lessonId, @Nullable String outletRoomId);
}
