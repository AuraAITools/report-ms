package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.repositories.StudentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddStudentsToLessonUseCaseImpl implements AddStudentsToLessonUseCase {
    private final StudentLessonLinkRepository studentLessonLinkRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<StudentLessonLink> addStudentsToLesson(String lessonId, List<String> studentIds, String tenantId) {
        if (studentIds.isEmpty()) return emptyList();
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("no lesson id found"));
        List<Student> students = studentRepository.findAllByIds(studentIds.stream().map(UUID::fromString).toList());
        List<StudentLessonLink> studentLessonLinks = students.stream()
                .map(student -> StudentLessonLink
                        .builder()
                        .studentId(student.getId())
                        .lessonId(lesson.getId())
                        .tenantId(tenantId)
                        .state(StudentLessonLink.STATE.REGISTERED)
                        .attendanceState(StudentLessonLink.ATTENDANCE_STATE.UNFILLED)
                        .build()).toList();

        return studentLessonLinkRepository.saveAll(studentLessonLinks);
    }
}
