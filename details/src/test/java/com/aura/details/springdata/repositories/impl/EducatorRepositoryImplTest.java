package com.aura.details.springdata.repositories.impl;

import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Educator repository implementation")
@ExtendWith(MockitoExtension.class)
class EducatorRepositoryImplTest {

    @Mock private JpaEducatorRepository jpaEducatorRepository;
    @Mock private JpaCourseRepository jpaCourseRepository;
    @Mock private JpaOutletRepository jpaOutletRepository;
    @Mock private JpaLevelRepository jpaLevelRepository;
    @Mock private JpaSubjectRepository jpaSubjectRepository;
    @Mock private JpaEducatorCourseLinkRepository jpaEducatorCourseLinkRepository;
    @Mock private JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository;
    @Mock private JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository;
    @Mock private JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository;

    @InjectMocks private EducatorRepositoryImpl educatorRepositoryImpl;

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
}
