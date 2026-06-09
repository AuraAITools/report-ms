package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
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

@DisplayName("CreateLessonTopicProficiencyUseCaseImpl")
@ExtendWith(MockitoExtension.class)
class CreateLessonTopicProficiencyUseCaseImplTest {

    @Mock
    private LessonTopicProficiencyRepository repository;

    @InjectMocks
    private CreateLessonTopicProficiencyUseCaseImpl useCase;

    @DisplayName("Should build a topic proficiency with the given report, topic, rating, tenant, and persist it")
    @Test
    void create_persistsBuiltEntity() {
        when(repository.save(any(LessonTopicProficiency.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LessonTopicProficiency result = useCase.create("report-1", "topic-a", 4, "tenant-X");

        ArgumentCaptor<LessonTopicProficiency> captor = ArgumentCaptor.forClass(LessonTopicProficiency.class);
        verify(repository).save(captor.capture());
        LessonTopicProficiency saved = captor.getValue();

        assertThat(saved.getStudentLessonReportId()).isEqualTo("report-1");
        assertThat(saved.getTopicId()).isEqualTo("topic-a");
        assertThat(saved.getRating()).isEqualTo(4);
        assertThat(saved.getTenantId()).isEqualTo("tenant-X");
        assertThat(result).isSameAs(saved);
    }

    @DisplayName("Should allow a null rating value")
    @Test
    void create_acceptsNullRating() {
        when(repository.save(any(LessonTopicProficiency.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        useCase.create("report-2", "topic-b", null, "tenant-Y");

        ArgumentCaptor<LessonTopicProficiency> captor = ArgumentCaptor.forClass(LessonTopicProficiency.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getRating()).isNull();
    }
}
