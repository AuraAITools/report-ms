package com.aura.details.services;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.usecases.lesson_groups.AddStudentsToLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.CreateLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.DeleteLessonGroupUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("LessonGroupService")
@ExtendWith(MockitoExtension.class)
class LessonGroupServiceTest {

    @Mock
    private CreateLessonGroupUseCase createLessonGroupUseCase;
    @Mock
    private AddStudentsToLessonGroupUseCase addStudentsToLessonGroupUseCase;
    @Mock
    private DeleteLessonGroupUseCase deleteLessonGroupUseCase;
    @Mock
    private LessonGroupRepository lessonGroupRepository;

    @InjectMocks
    private LessonGroupService lessonGroupService;

    @DisplayName("Should delegate createLessonGroup to use case")
    @Test
    void createLessonGroup_delegatesToUseCase() {
        String lessonId = UUID.randomUUID().toString();
        LessonGroup input = LessonGroup.builder().name("Group A").build();
        LessonGroup created = LessonGroup.builder().id(UUID.randomUUID().toString()).name("Group A").lessonId(lessonId).build();
        when(createLessonGroupUseCase.createLessonGroup(input, lessonId)).thenReturn(created);

        LessonGroup result = lessonGroupService.createLessonGroup(input, lessonId);

        assertThat(result).isSameAs(created);
        verify(createLessonGroupUseCase).createLessonGroup(input, lessonId);
    }

    @DisplayName("Should delegate addStudentsToLessonGroup to use case")
    @Test
    void addStudentsToLessonGroup_delegatesToUseCase() {
        String lessonGroupId = UUID.randomUUID().toString();
        String tenantId = "tenant-1";
        List<String> studentIds = List.of("student-1", "student-2");
        StudentLessonLink link1 = StudentLessonLink.builder().studentId("student-1").build();
        StudentLessonLink link2 = StudentLessonLink.builder().studentId("student-2").build();
        List<StudentLessonLink> links = List.of(link1, link2);
        when(addStudentsToLessonGroupUseCase.addStudentsToLessonGroup(lessonGroupId, studentIds, tenantId)).thenReturn(links);

        List<StudentLessonLink> result = lessonGroupService.addStudentsToLessonGroup(lessonGroupId, studentIds, tenantId);

        assertThat(result).isSameAs(links);
        verify(addStudentsToLessonGroupUseCase).addStudentsToLessonGroup(lessonGroupId, studentIds, tenantId);
    }

    @DisplayName("Should return findById result from repository")
    @Test
    void findById_returnsRepositoryResult() {
        UUID id = UUID.randomUUID();
        LessonGroup group = LessonGroup.builder().id(id.toString()).name("Group B").build();
        when(lessonGroupRepository.findById(id)).thenReturn(group);

        LessonGroup result = lessonGroupService.findById(id);

        assertThat(result).isSameAs(group);
        verify(lessonGroupRepository).findById(id);
    }

    @DisplayName("Should return null when findById misses in the repository")
    @Test
    void findById_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(lessonGroupRepository.findById(id)).thenReturn(null);

        LessonGroup result = lessonGroupService.findById(id);

        assertThat(result).isNull();
        verify(lessonGroupRepository).findById(id);
    }

    @DisplayName("Should return findAllByLessonId result from repository")
    @Test
    void findAllByLessonId_returnsRepositoryResult() {
        String lessonId = UUID.randomUUID().toString();
        LessonGroup g1 = LessonGroup.builder().id(UUID.randomUUID().toString()).lessonId(lessonId).build();
        LessonGroup g2 = LessonGroup.builder().id(UUID.randomUUID().toString()).lessonId(lessonId).build();
        when(lessonGroupRepository.findAllByLessonId(lessonId)).thenReturn(List.of(g1, g2));

        List<LessonGroup> result = lessonGroupService.findAllByLessonId(lessonId);

        assertThat(result).containsExactly(g1, g2);
        verify(lessonGroupRepository).findAllByLessonId(lessonId);
    }

    @DisplayName("Should return empty list when findAllByLessonId finds no groups")
    @Test
    void findAllByLessonId_returnsEmptyList() {
        String lessonId = UUID.randomUUID().toString();
        when(lessonGroupRepository.findAllByLessonId(lessonId)).thenReturn(List.of());

        List<LessonGroup> result = lessonGroupService.findAllByLessonId(lessonId);

        assertThat(result).isEmpty();
        verify(lessonGroupRepository).findAllByLessonId(lessonId);
    }

    @DisplayName("Should clean up children before deleting the lesson group")
    @Test
    void deleteById_cleansChildrenThenDeletesGroup() {
        String id = UUID.randomUUID().toString();

        lessonGroupService.deleteById(id);

        var ordered = inOrder(lessonGroupRepository, deleteLessonGroupUseCase);
        ordered.verify(lessonGroupRepository).deleteAllLessonGroupChildrenByLessonGroupId(id);
        ordered.verify(deleteLessonGroupUseCase).deleteById(id);
    }
}
