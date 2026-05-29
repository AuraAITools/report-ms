package com.aura.details.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;


import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.usecases.educators.DeleteEducatorUseCase;
import com.aura.core.usecases.educators.EducatorQueryUseCase;
import com.aura.core.usecases.educators.UpdateEducatorUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EducatorService")
class EducatorServiceTest {

    @Mock
    private EducatorQueryUseCase queryUseCase;
    @Mock
    private UpdateEducatorUseCase updateUseCase;
    @Mock
    private DeleteEducatorUseCase deleteUseCase;
    @Mock
    private EducatorRepository educatorRepository;
    @InjectMocks
    private EducatorService service;

    @Test
    @DisplayName("Should delegate findById to the query use case")
    void findByIdDelegates() {
        Educator e = Educator.builder().id("a").build();
        when(queryUseCase.findById("a")).thenReturn(e);
        assertThat(service.findById("a")).isEqualTo(e);
    }

    @Test
    @DisplayName("Should delegate findAllByTenantId to the query use case")
    void findAllByTenantIdDelegates() {
        when(queryUseCase.findAllByTenantId("t")).thenReturn(List.of());
        assertThat(service.findAllByTenantId("t")).isEmpty();
    }

    @Test
    @DisplayName("Should delegate updateById to the update use case")
    void updateByIdDelegates() {
        Educator e = Educator.builder().id("a").name("New").build();
        when(updateUseCase.update("a", "New", null, null, null, null)).thenReturn(e);
        assertThat(service.updateById("a", "New", null, null, null, null)).isEqualTo(e);
    }

    @Test
    @DisplayName("Should clean all link rows before calling delete use case on deleteById")
    void deleteByIdClearsLinksThenDeletes() {
        service.deleteById("eid");

        InOrder order = inOrder(educatorRepository, deleteUseCase);
        order.verify(educatorRepository).deleteAllEducatorCourseLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllEducatorLessonLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllLevelEducatorLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllOutletEducatorLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllSubjectEducatorLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllAccountEducatorLinksByEducatorId("eid");
        order.verify(deleteUseCase).deleteById("eid");
    }
}
