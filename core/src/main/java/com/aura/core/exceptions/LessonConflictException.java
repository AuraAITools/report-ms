package com.aura.core.exceptions;

import java.util.List;
import org.jspecify.annotations.Nullable;

public class LessonConflictException extends RuntimeException {

    public enum ConflictType { ROOM, STUDENT, EDUCATOR }

    private final ConflictType type;
    private final List<String> conflictingLessonIds;
    private final @Nullable String subjectId;

    public LessonConflictException(ConflictType type,
                                   List<String> conflictingLessonIds,
                                   @Nullable String subjectId) {
        super(buildMessage(type, conflictingLessonIds, subjectId));
        this.type = type;
        this.conflictingLessonIds = List.copyOf(conflictingLessonIds);
        this.subjectId = subjectId;
    }

    public ConflictType getType() { return type; }
    public List<String> getConflictingLessonIds() { return conflictingLessonIds; }
    public @Nullable String getSubjectId() { return subjectId; }

    private static String buildMessage(ConflictType type, List<String> ids, @Nullable String subjectId) {
        return switch (type) {
            case ROOM -> "Room conflict with lessons: " + ids;
            case STUDENT -> "Student " + subjectId + " has conflicting lessons: " + ids;
            case EDUCATOR -> "Educator " + subjectId + " has conflicting lessons: " + ids;
        };
    }
}
