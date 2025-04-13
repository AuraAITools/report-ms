package com.reportai.www.reportapi.services.analytics;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonHomeworkCompletion;
import com.reportai.www.reportapi.repositories.analytics.LessonHomeworkCompletionRepository;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalyticsService {
    private final LessonsService lessonsService;

    private final StudentsService studentsService;

    private final LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository;

    @Autowired
    public AnalyticsService(LessonsService lessonsService, StudentsService studentsService, LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository) {
        this.lessonsService = lessonsService;
        this.studentsService = studentsService;
        this.lessonHomeworkCompletionRepository = lessonHomeworkCompletionRepository;
    }

    /**
     * TODO: tighten logic by ensuring that LessonHomeworkCompletion can only be created by students WITHIN the lesson
     *
     * @param studentId
     * @param lessonId
     * @param lessonHomeworkCompletion
     * @return
     */
    @Transactional
    public LessonHomeworkCompletion createLessonHomeworkCompletion(UUID studentId, UUID lessonId, LessonHomeworkCompletion lessonHomeworkCompletion) {
        Student student = studentsService.findById(studentId);
        Lesson lesson = lessonsService.findById(lessonId);
//        LessonStudentCompositeKey compositeKey = new LessonStudentCompositeKey(studentId, lessonId);
//        boolean exists = lessonHomeworkCompletionRepository.existsById(compositeKey);
//        if (exists) {
//            throw new EntityExistsException("Homework completion already exists for lesson ID " +
//                    lessonId + " and student ID " + studentId);
//        }
        // FIXME: to take a look at
//        lessonHomeworkCompletion
//                .forLesson(lesson)
//                .forStudent(student)
//                .setId(compositeKey);
        return lessonHomeworkCompletionRepository.save(lessonHomeworkCompletion);
    }
}
