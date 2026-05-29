package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.courses.Course;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
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

@DisplayName("Course repository impl")
@ExtendWith(MockitoExtension.class)
class CourseRepositoryImplTest {

    @Mock
    private JpaCourseRepository jpaCourseRepository;
    @Mock
    private JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository;
    @Mock
    private JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private CourseRepositoryImpl repository;

    @DisplayName("Should attach managed outlet and level proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesOutletAndLevelRefs() {
        UUID outletId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        OutletModel outletProxy = OutletModel.builder().id(outletId).build();
        LevelModel levelProxy = LevelModel.builder().id(levelId).build();
        when(entityManager.getReference(OutletModel.class, outletId)).thenReturn(outletProxy);
        when(entityManager.getReference(LevelModel.class, levelId)).thenReturn(levelProxy);
        when(jpaCourseRepository.save(any(CourseModel.class))).thenReturn(new CourseModel());

        Course course = Course.builder()
                .outletId(outletId.toString())
                .levelId(levelId.toString())
                .name("Maths 101")
                .build();
        repository.save(course);

        ArgumentCaptor<CourseModel> captor = ArgumentCaptor.forClass(CourseModel.class);
        verify(jpaCourseRepository).save(captor.capture());
        assertThat(captor.getValue().getOutlet()).isSameAs(outletProxy);
        assertThat(captor.getValue().getLevel()).isSameAs(levelProxy);
        assertThat(captor.getValue().getName()).isEqualTo("Maths 101");
    }

    @DisplayName("Should return all courses whose outlet matches the given outlet id")
    @Test
    void findAllByOutletIdReturnsMatchingCourses() {
        UUID outletId = UUID.randomUUID();
        CourseModel m1 = new CourseModel();
        m1.setId(UUID.randomUUID());
        m1.setName("Course A");
        CourseModel m2 = new CourseModel();
        m2.setId(UUID.randomUUID());
        m2.setName("Course B");
        when(jpaCourseRepository.findByOutlet_Id(outletId)).thenReturn(List.of(m1, m2));

        List<Course> result = repository.findAllByOutletId(outletId.toString());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Course::getName).containsExactly("Course A", "Course B");
    }

    @DisplayName("Should return an empty list when no courses match the outlet id")
    @Test
    void findAllByOutletIdReturnsEmptyWhenNoMatches() {
        UUID outletId = UUID.randomUUID();
        when(jpaCourseRepository.findByOutlet_Id(outletId)).thenReturn(List.of());

        assertThat(repository.findAllByOutletId(outletId.toString())).isEmpty();
    }

    @DisplayName("Should return all courses matching the given ids")
    @Test
    void findAllByIds_returnsCourses() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        CourseModel m1 = new CourseModel();
        m1.setId(id1);
        m1.setName("A");
        CourseModel m2 = new CourseModel();
        m2.setId(id2);
        m2.setName("B");
        when(jpaCourseRepository.findAllById(List.of(id1, id2))).thenReturn(List.of(m1, m2));

        List<Course> result = repository.findAllByIds(List.of(id1.toString(), id2.toString()));
        assertThat(result).extracting(Course::getName).containsExactly("A", "B");
    }

    @DisplayName("Should return empty list when id list is empty for findAllByIds")
    @Test
    void findAllByIds_emptyList_returnsEmpty() {
        assertThat(repository.findAllByIds(List.of())).isEmpty();
    }

    @Test
    @DisplayName("Should return empty map when no student ids provided to findAllByStudentIds")
    void findAllByStudentIdsEmpty() {
        assertThat(repository.findAllByStudentIds(List.of())).isEmpty();
    }

    @Test
    @DisplayName("Should group courses by studentId")
    void findAllByStudentIdsGroups() {
        UUID s1 = UUID.randomUUID();
        UUID s2 = UUID.randomUUID();
        CourseModel c1 = CourseModel.builder().build();
        CourseModel c2 = CourseModel.builder().build();
        when(jpaStudentCourseLinkRepository.findCoursesByStudentIds(List.of(s1, s2)))
                .thenReturn(List.<Object[]>of(
                        new Object[]{s1, c1},
                        new Object[]{s1, c2},
                        new Object[]{s2, c1}
                ));

        Map<String, List<Course>> result = repository.findAllByStudentIds(List.of(s1.toString(), s2.toString()));

        assertThat(result.get(s1.toString())).hasSize(2);
        assertThat(result.get(s2.toString())).hasSize(1);
    }

    @Test
    @DisplayName("Should return empty map when no educator ids provided to findAllByEducatorIds")
    void findAllByEducatorIdsEmpty() {
        assertThat(repository.findAllByEducatorIds(List.of())).isEmpty();
    }

    @Test
    @DisplayName("Should group courses by educatorId")
    void findAllByEducatorIdsGroups() {
        UUID e1 = UUID.randomUUID();
        UUID e2 = UUID.randomUUID();
        CourseModel c1 = CourseModel.builder().build();
        CourseModel c2 = CourseModel.builder().build();
        when(jpaEducatorCourseLinkRepository.findCoursesByEducatorIds(List.of(e1, e2)))
                .thenReturn(List.<Object[]>of(
                        new Object[]{e1, c1},
                        new Object[]{e1, c2},
                        new Object[]{e2, c1}
                ));

        Map<String, List<Course>> result = repository.findAllByEducatorIds(List.of(e1.toString(), e2.toString()));

        assertThat(result.get(e1.toString())).hasSize(2);
        assertThat(result.get(e2.toString())).hasSize(1);
    }
}
