package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.PriceRecord;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.PriceRecordModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaPriceRecordRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
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

@DisplayName("Price record repository impl")
@ExtendWith(MockitoExtension.class)
class PriceRecordRepositoryImplTest {

    @Mock
    private JpaPriceRecordRepository jpaPriceRecordRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private PriceRecordRepositoryImpl repository;

    @DisplayName("Should attach managed course proxy via EntityManager.getReference before persisting")
    @Test
    void saveAttachesCourseRef() {
        UUID courseId = UUID.randomUUID();
        CourseModel courseProxy = CourseModel.builder().id(courseId).build();
        when(entityManager.getReference(CourseModel.class, courseId)).thenReturn(courseProxy);
        when(jpaPriceRecordRepository.save(any(PriceRecordModel.class))).thenReturn(new PriceRecordModel());

        PriceRecord priceRecord = PriceRecord.builder()
                .courseId(courseId.toString())
                .price(99.5)
                .frequency(PriceRecord.FREQUENCY.MONTHLY)
                .build();
        repository.save(priceRecord);

        ArgumentCaptor<PriceRecordModel> captor = ArgumentCaptor.forClass(PriceRecordModel.class);
        verify(jpaPriceRecordRepository).save(captor.capture());
        assertThat(captor.getValue().getCourse()).isSameAs(courseProxy);
        assertThat(captor.getValue().getPrice()).isEqualTo(99.5);
    }
}
