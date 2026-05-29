package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.educators.Educator;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Educator repository implementation")
@ExtendWith(MockitoExtension.class)
class EducatorRepositoryImplTest {

    @Mock
    private JpaEducatorRepository jpaEducatorRepository;
    @Mock
    private JpaCourseRepository jpaCourseRepository;
    @Mock
    private JpaOutletRepository jpaOutletRepository;
    @Mock
    private JpaLevelRepository jpaLevelRepository;
    @Mock
    private JpaSubjectRepository jpaSubjectRepository;
    @Mock
    private JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository;
    @Mock
    private JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository;
    @Mock
    private JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository;
    @Mock
    private JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository;
    @Mock
    private JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;
    @Mock
    private JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository;

    @InjectMocks
    private EducatorRepositoryImpl educatorRepositoryImpl;

    @DisplayName("Should return map of educator ID to user ID")
    @Test
    void findUserIdsByEducatorIds_returnsMappedUserIds() {
        UUID educatorId1 = UUID.randomUUID();
        UUID educatorId2 = UUID.randomUUID();
        List<String> educatorIds = List.of(educatorId1.toString(), educatorId2.toString());

        when(jpaEducatorRepository.findUserIdsByIds(List.of(educatorId1, educatorId2)))
                .thenReturn(List.of(
                        new Object[]{educatorId1, "user-aaa"},
                        new Object[]{educatorId2, "user-bbb"}
                ));

        Map<String, String> result = educatorRepositoryImpl.findUserIdsByEducatorIds(educatorIds);

        assertThat(result).containsEntry(educatorId1.toString(), "user-aaa");
        assertThat(result).containsEntry(educatorId2.toString(), "user-bbb");
        assertThat(result).hasSize(2);
    }

    @DisplayName("Should return empty map when educator list is empty")
    @Test
    void findUserIdsByEducatorIds_emptyList_returnsEmptyMap() {
        Map<String, String> result = educatorRepositoryImpl.findUserIdsByEducatorIds(List.of());

        assertThat(result).isEmpty();
    }

    @DisplayName("Should group educators by course id from link table query")
    @Test
    void findAllByCourseIds_groupsEducatorsByCourse() {
        UUID courseId1 = UUID.randomUUID();
        UUID courseId2 = UUID.randomUUID();
        UUID educator1Id = UUID.randomUUID();
        UUID educator2Id = UUID.randomUUID();
        UUID educator3Id = UUID.randomUUID();
        EducatorModel e1 = EducatorModel.builder().id(educator1Id).build();
        EducatorModel e2 = EducatorModel.builder().id(educator2Id).build();
        EducatorModel e3 = EducatorModel.builder().id(educator3Id).build();

        when(jpaEducatorCourseLinkRepository.findEducatorsByCourseIds(List.of(courseId1, courseId2)))
                .thenReturn(List.of(
                        new Object[]{courseId1, e1},
                        new Object[]{courseId1, e2},
                        new Object[]{courseId2, e3}
                ));

        Map<String, List<Educator>> result = educatorRepositoryImpl
                .findAllByCourseIds(List.of(courseId1.toString(), courseId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(courseId1.toString())).extracting(Educator::getId)
                .containsExactly(educator1Id.toString(), educator2Id.toString());
        assertThat(result.get(courseId2.toString())).extracting(Educator::getId)
                .containsExactly(educator3Id.toString());
    }

    @DisplayName("Should return empty map when course id list is empty")
    @Test
    void findAllByCourseIds_emptyList_returnsEmptyMap() {
        assertThat(educatorRepositoryImpl.findAllByCourseIds(List.of())).isEmpty();
    }

    @DisplayName("Should return all educators for tenant")
    @Test
    void findAllByTenantId_returnsAll() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel model = EducatorModel.builder().id(educatorId).name("Alice").build();
        when(jpaEducatorRepository.findByTenantId("tenant-1")).thenReturn(List.of(model));

        List<Educator> result = educatorRepositoryImpl.findAllByTenantId("tenant-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(educatorId.toString());
        assertThat(result.get(0).getName()).isEqualTo("Alice");
    }

    @DisplayName("Should delegate deleteById to JPA repository with parsed UUID")
    @Test
    void deleteById_delegates() {
        String id = "00000000-0000-0000-0000-000000000001";

        educatorRepositoryImpl.deleteById(id);

        verify(jpaEducatorRepository).deleteById(UUID.fromString(id));
    }

    @DisplayName("Should delegate deleteAllEducatorLessonLinksByEducatorId to JPA repository")
    @Test
    void deleteAllEducatorLessonLinksByEducatorId_delegates() {
        UUID educatorId = UUID.randomUUID();

        educatorRepositoryImpl.deleteAllEducatorLessonLinksByEducatorId(educatorId.toString());

        verify(jpaEducatorLessonLinkRepository).deleteAllByEducatorId(educatorId);
    }

    @DisplayName("Should delegate deleteAllLevelEducatorLinksByEducatorId to JPA repository")
    @Test
    void deleteAllLevelEducatorLinksByEducatorId_delegates() {
        UUID educatorId = UUID.randomUUID();

        educatorRepositoryImpl.deleteAllLevelEducatorLinksByEducatorId(educatorId.toString());

        verify(jpaLevelEducatorLinkRepository).deleteAllByEducatorId(educatorId);
    }

    @DisplayName("Should delegate deleteAllOutletEducatorLinksByEducatorId to JPA repository")
    @Test
    void deleteAllOutletEducatorLinksByEducatorId_delegates() {
        UUID educatorId = UUID.randomUUID();

        educatorRepositoryImpl.deleteAllOutletEducatorLinksByEducatorId(educatorId.toString());

        verify(jpaOutletEducatorLinkRepository).deleteAllByEducatorId(educatorId);
    }

    @DisplayName("Should delegate deleteAllSubjectEducatorLinksByEducatorId to JPA repository")
    @Test
    void deleteAllSubjectEducatorLinksByEducatorId_delegates() {
        UUID educatorId = UUID.randomUUID();

        educatorRepositoryImpl.deleteAllSubjectEducatorLinksByEducatorId(educatorId.toString());

        verify(jpaSubjectEducatorLinkRepository).deleteAllByEducatorId(educatorId);
    }

    @DisplayName("Should delegate deleteAllAccountEducatorLinksByEducatorId to JPA repository")
    @Test
    void deleteAllAccountEducatorLinksByEducatorId_delegates() {
        UUID educatorId = UUID.randomUUID();

        educatorRepositoryImpl.deleteAllAccountEducatorLinksByEducatorId(educatorId.toString());

        verify(jpaAccountEducatorLinkRepository).deleteAllByEducatorId(educatorId);
    }

    @DisplayName("Should delegate deleteAllEducatorCourseLinksByEducatorId to JPA repository")
    @Test
    void deleteAllEducatorCourseLinksByEducatorId_delegates() {
        UUID educatorId = UUID.randomUUID();

        educatorRepositoryImpl.deleteAllEducatorCourseLinksByEducatorId(educatorId.toString());

        verify(jpaEducatorCourseLinkRepository).deleteAllByEducatorId(educatorId);
    }
}
