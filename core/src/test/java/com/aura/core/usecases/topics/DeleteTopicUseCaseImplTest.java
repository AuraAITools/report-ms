package com.aura.core.usecases.topics;

import com.aura.core.repositories.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete topic use case")
@ExtendWith(MockitoExtension.class)
class DeleteTopicUseCaseImplTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private DeleteTopicUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the topic repository")
    void deletesViaRepository() {
        useCase.deleteById("topic-1");

        verify(topicRepository).deleteById("topic-1");
    }
}
