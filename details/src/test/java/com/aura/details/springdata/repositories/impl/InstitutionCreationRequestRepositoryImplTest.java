package com.aura.details.springdata.repositories.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.details.springdata.models.institutions.InstitutionCreationRequestModel;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionCreationRequestRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("InstitutionCreationRequestRepositoryImpl")
@ExtendWith(MockitoExtension.class)
class InstitutionCreationRequestRepositoryImplTest {

    @Mock
    private JpaInstitutionCreationRequestRepository jpaRepository;
    @InjectMocks
    private InstitutionCreationRequestRepositoryImpl repository;

    private InstitutionCreationRequest validRequest(String id) {
        return InstitutionCreationRequest.builder()
                .id(id)
                .institutionName("Acme Tutors")
                .institutionEmail("contact@acme.edu")
                .institutionContactNumber("+6512345678")
                .institutionAddress("1 Acme Way")
                .institutionUen("UEN-001")
                .requesterFirstName("Alice")
                .requesterLastName("Lee")
                .requesterEmail("alice@acme.edu")
                .requesterContactNumber("+6587654321")
                .state(InstitutionCreationRequest.STATE.REQUESTED)
                .build();
    }

    @Test
    @DisplayName("Should round-trip domain entity through MapStruct on save")
    void saveRoundTripsViaMapper() {
        UUID id = UUID.randomUUID();
        InstitutionCreationRequest input = validRequest(id.toString());
        when(jpaRepository.save(any(InstitutionCreationRequestModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        InstitutionCreationRequest result = repository.save(input);

        ArgumentCaptor<InstitutionCreationRequestModel> captor =
                ArgumentCaptor.forClass(InstitutionCreationRequestModel.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(id);
        assertThat(captor.getValue().getInstitutionName()).isEqualTo("Acme Tutors");
        assertThat(captor.getValue().getState()).isEqualTo(InstitutionCreationRequestModel.STATE.REQUESTED);
        assertThat(result.getInstitutionName()).isEqualTo("Acme Tutors");
    }

    @Test
    @DisplayName("Should return null when findById finds nothing")
    void findByIdReturnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(repository.findById(id.toString())).isNull();
    }

    @Test
    @DisplayName("Should delegate existsByInstitutionNameAndState to Spring Data derived method")
    void existsByInstitutionNameAndStateDelegates() {
        when(jpaRepository.existsByInstitutionNameAndState("Acme", InstitutionCreationRequestModel.STATE.REQUESTED))
                .thenReturn(true);

        assertThat(repository.existsByInstitutionNameAndState("Acme", InstitutionCreationRequest.STATE.REQUESTED))
                .isTrue();
    }

    @Test
    @DisplayName("Should delegate existsByInstitutionEmailAndState to Spring Data derived method")
    void existsByInstitutionEmailAndStateDelegates() {
        when(jpaRepository.existsByInstitutionEmailAndState("a@b.com", InstitutionCreationRequestModel.STATE.REQUESTED))
                .thenReturn(false);

        assertThat(repository.existsByInstitutionEmailAndState("a@b.com", InstitutionCreationRequest.STATE.REQUESTED))
                .isFalse();
    }
}
