package com.aura.core.usecases.educators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import com.aura.core.entities.educators.Educator;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.EducatorRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateEducatorUseCaseImpl")
class UpdateEducatorUseCaseImplTest {

    @Mock
    private EducatorRepository repo;
    @InjectMocks
    private UpdateEducatorUseCaseImpl useCase;

    @Test
    @DisplayName("Should update only the non-null fields and persist")
    void updatesNonNullFields() {
        Educator existing = Educator.builder()
                .id("a")
                .name("Old")
                .email("old@e.com")
                .startDate(LocalDate.of(2020, 1, 1))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .employmentType(Educator.EMPLOYMENT_TYPE.FULL_TIME)
                .build();
        when(repo.findById("a")).thenReturn(existing);
        when(repo.save(any(Educator.class))).thenAnswer(inv -> inv.getArgument(0));

        Educator updated = useCase.update(
                "a",
                "New",
                LocalDate.of(2021, 2, 2),
                null,
                null,
                Educator.EMPLOYMENT_TYPE.PART_TIME);

        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getStartDate()).isEqualTo(LocalDate.of(2021, 2, 2));
        assertThat(updated.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(updated.getEmail()).isEqualTo("old@e.com");
        assertThat(updated.getEmploymentType()).isEqualTo(Educator.EMPLOYMENT_TYPE.PART_TIME);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when educator does not exist")
    void throwsWhenMissing() {
        when(repo.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.update("missing", "x", null, null, null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
