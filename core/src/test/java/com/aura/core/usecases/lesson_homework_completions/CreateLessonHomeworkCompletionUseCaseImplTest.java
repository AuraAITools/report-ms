package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CreateLessonHomeworkCompletionUseCaseImpl")
@ExtendWith(MockitoExtension.class)
class CreateLessonHomeworkCompletionUseCaseImplTest {

    @Mock
    private LessonHomeworkCompletionRepository repository;

    @InjectMocks
    private CreateLessonHomeworkCompletionUseCaseImpl useCase;

    @DisplayName("Should build a homework completion with the given report id, completion, tenant, and persist it")
    @Test
    void create_persistsBuiltEntity() {
        when(repository.save(any(LessonHomeworkCompletion.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LessonHomeworkCompletion result = useCase.create("report-1", 80, "tenant-X");

        ArgumentCaptor<LessonHomeworkCompletion> captor = ArgumentCaptor.forClass(LessonHomeworkCompletion.class);
        verify(repository).save(captor.capture());
        LessonHomeworkCompletion saved = captor.getValue();

        assertThat(saved.getStudentLessonReportId()).isEqualTo("report-1");
        assertThat(saved.getCompletion()).isEqualTo(80);
        assertThat(saved.getTenantId()).isEqualTo("tenant-X");
        assertThat(result).isSameAs(saved);
    }

    @DisplayName("Should allow a null completion value")
    @Test
    void create_acceptsNullCompletion() {
        when(repository.save(any(LessonHomeworkCompletion.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        useCase.create("report-2", null, "tenant-Y");

        ArgumentCaptor<LessonHomeworkCompletion> captor = ArgumentCaptor.forClass(LessonHomeworkCompletion.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getCompletion()).isNull();
    }
}
