package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.DeleteOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.outlets.UpdateOutletUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Outlet service")
@ExtendWith(MockitoExtension.class)
class OutletServiceTest {

    @Mock
    private CreateOutletUseCase createOutletUseCase;
    @Mock
    private OutletQueryUseCase outletQueryUseCase;
    @Mock
    private UpdateOutletUseCase updateOutletUseCase;
    @Mock
    private DeleteOutletUseCase deleteOutletUseCase;
    @Mock
    private CourseService courseService;
    @Mock
    private OutletRepository outletRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private RelationshipAuthorizationService relationshipService;

    @InjectMocks
    private OutletService outletService;

    @DisplayName("Should write all three structural relationships in a single batch")
    @Test
    void createOutlet_writesStructuralRelationshipsAsBatch() {
        Outlet input = Outlet.builder().name("Test Outlet").build();
        Outlet created = Outlet.builder().id("outlet-123").name("Test Outlet").tenantId("tenant-789").build();
        when(createOutletUseCase.createOutlet(any())).thenReturn(created);

        Outlet result = outletService.createOutlet(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationships(argThat(list ->
                list.containsAll(List.of(
                        new RelationshipAuthorizationService.Relationship(
                                "outlet", "outlet-123", "user_group", "outlet_user_group", "outlet-123"),
                        new RelationshipAuthorizationService.Relationship(
                                "outlet_user_group", "outlet-123", "parent", "system_user_group", "aura"),
                        new RelationshipAuthorizationService.Relationship(
                                "outlet", "outlet-123", "parent", "institution", "tenant-789")))
                        && list.size() == 3));
        verifyNoMoreInteractions(relationshipService);
    }

    @DisplayName("Should delegate findAllByTenantId to the query use case")
    @Test
    void findAllByTenantId_delegatesToQueryUseCase() {
        Outlet o1 = Outlet.builder().id("o1").name("Bukit Timah").build();
        Outlet o2 = Outlet.builder().id("o2").name("Tampines").build();
        when(outletQueryUseCase.findAllByTenantId("tenant-1")).thenReturn(List.of(o1, o2));

        assertThat(outletService.findAllByTenantId("tenant-1")).containsExactly(o1, o2);
    }

    @DisplayName("Should delegate updateOutlet to the update use case")
    @Test
    void updateOutlet_delegatesToUpdateUseCase() {
        Outlet input = Outlet.builder().id("o1").name("Renamed").build();
        Outlet updated = Outlet.builder().id("o1").name("Renamed").tenantId("tenant-1").build();
        when(updateOutletUseCase.updateOutlet(input)).thenReturn(updated);

        assertThat(outletService.updateOutlet(input)).isEqualTo(updated);
    }

    @DisplayName("Should return null from updateOutlet when use case returns null")
    @Test
    void updateOutlet_returnsNullWhenUseCaseReturnsNull() {
        Outlet input = Outlet.builder().id("missing").build();
        when(updateOutletUseCase.updateOutlet(input)).thenReturn(null);

        assertThat(outletService.updateOutlet(input)).isNull();
    }

    @DisplayName("Should cascade-delete child courses then wipe spicedb")
    @Test
    void deleteById_cascadeDeletesCoursesAndCleansUp() {
        Course c1 = Course.builder().id("course-a").outletId("outlet-1").build();
        Course c2 = Course.builder().id("course-b").outletId("outlet-1").build();
        when(courseRepository.findAllByOutletId("outlet-1")).thenReturn(List.of(c1, c2));

        outletService.deleteById("outlet-1");

        verify(courseService).deleteById("course-a");
        verify(courseService).deleteById("course-b");
        verify(deleteOutletUseCase).deleteById("outlet-1");
        verify(relationshipService).deleteAllRelationshipsForResource("outlet", "outlet-1");
        verify(relationshipService).deleteAllRelationshipsForResource("outlet_user_group", "outlet-1");
    }
}
