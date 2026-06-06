package com.aura.details.services;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.authorization.GroupResourcePermission;
import com.aura.core.usecases.authorization.ResourcePermission;
import com.aura.core.usecases.authorization.ResourceRelations;
import com.aura.core.usecases.authorization.UserGroupRelation;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("User group resource resolver")
@ExtendWith(MockitoExtension.class)
class UserGroupResourceResolverTest {

    @Mock InstitutionRepository institutionRepository;
    @Mock OutletRepository outletRepository;
    @Mock CourseRepository courseRepository;
    @Mock LessonRepository lessonRepository;
    @Mock AccountRepository accountRepository;
    @Mock LevelRepository levelRepository;
    @Mock SubjectRepository subjectRepository;
    @Mock TopicRepository topicRepository;
    @Mock MaterialRepository materialRepository;

    @InjectMocks
    UserGroupResourceResolver resolver;

    @Test
    @DisplayName("Should hydrate displayName from outlet repository when tenant matches")
    void hydratesOutletDisplayName() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("Main Outlet").tenantId("inst-1").build();
        when(outletRepository.findById("outlet-1")).thenReturn(outlet);

        List<GroupResourcePermission> result = resolver.resolveAndFilter(
                List.of(new ResourcePermission("outlet_user_group", "outlet-1", List.of("view"))),
                "inst-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).displayName()).isEqualTo("Main Outlet");
        assertThat(result.get(0).resourceType()).isEqualTo("outlet_user_group");
        assertThat(result.get(0).resourceId()).isEqualTo("outlet-1");
        assertThat(result.get(0).permissions()).containsExactly("view");
    }

    @Test
    @DisplayName("Should drop resource whose tenantId does not match the requested institution")
    void filtersByTenant() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("Main").tenantId("other-inst").build();
        when(outletRepository.findById("outlet-1")).thenReturn(outlet);

        List<GroupResourcePermission> result = resolver.resolveAndFilter(
                List.of(new ResourcePermission("outlet_user_group", "outlet-1", List.of("view"))),
                "inst-1");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should drop resource that no longer exists in the database")
    void filtersMissing() {
        when(outletRepository.findById("ghost")).thenReturn(null);

        List<GroupResourcePermission> result = resolver.resolveAndFilter(
                List.of(new ResourcePermission("outlet_user_group", "ghost", List.of("view"))),
                "inst-1");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should keep institution_user_group only when id equals the requested institution")
    void filtersInstitutionGroup() {
        Institution inst = Institution.builder().id("inst-1").name("Acme Schools").tenantId("inst-1").build();
        when(institutionRepository.findById("inst-1")).thenReturn(inst);

        List<GroupResourcePermission> match = resolver.resolveAndFilter(
                List.of(new ResourcePermission("institution_user_group", "inst-1", List.of("view"))),
                "inst-1");
        List<GroupResourcePermission> mismatch = resolver.resolveAndFilter(
                List.of(new ResourcePermission("institution_user_group", "other", List.of("view"))),
                "inst-1");

        assertThat(match).hasSize(1);
        assertThat(match.get(0).displayName()).isEqualTo("Acme Schools");
        assertThat(mismatch).isEmpty();
    }

    @Test
    @DisplayName("Should concatenate firstName + lastName for account_user_group displayName")
    void hydratesAccountDisplayName() {
        Account account = Account.builder().id("acc-1").firstName("Alice").lastName("Smith").tenantId("inst-1").build();
        when(accountRepository.findById("acc-1")).thenReturn(account);

        List<GroupResourcePermission> result = resolver.resolveAndFilter(
                List.of(new ResourcePermission("account_user_group", "acc-1", List.of("view"))),
                "inst-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).displayName()).isEqualTo("Alice Smith");
    }

    @Test
    @DisplayName("Should resolve lesson_user_group via UUID-typed repository")
    void hydratesLessonDisplayName() {
        UUID lessonId = UUID.randomUUID();
        Lesson lesson = Lesson.builder().id(lessonId.toString()).name("Algebra 101").tenantId("inst-1").build();
        when(lessonRepository.findById(lessonId)).thenReturn(lesson);

        List<GroupResourcePermission> result = resolver.resolveAndFilter(
                List.of(new ResourcePermission("lesson_user_group", lessonId.toString(), List.of("view"))),
                "inst-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).displayName()).isEqualTo("Algebra 101");
    }

    @Test
    @DisplayName("Should hydrate displayName + filter institution on relations input")
    void hydratesRelationsWithDisplayName() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("Main Outlet").tenantId("inst-1").build();
        Outlet otherOutlet = Outlet.builder().id("outlet-2").name("Other").tenantId("other-inst").build();
        when(outletRepository.findById("outlet-1")).thenReturn(outlet);
        when(outletRepository.findById("outlet-2")).thenReturn(otherOutlet);

        List<UserGroupRelation> result = resolver.resolveAndFilterRelations(List.of(
                new ResourceRelations("outlet_user_group", "outlet-1", List.of("admin", "group_admin")),
                new ResourceRelations("outlet_user_group", "outlet-2", List.of("viewer"))
        ), "inst-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).resourceId()).isEqualTo("outlet-1");
        assertThat(result.get(0).displayName()).isEqualTo("Main Outlet");
        assertThat(result.get(0).relations()).containsExactlyInAnyOrder("admin", "group_admin");
    }

    @Test
    @DisplayName("belongsToInstitution returns true when resource exists and tenant matches")
    void belongsToInstitutionTrue() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("X").tenantId("inst-1").build();
        when(outletRepository.findById("outlet-1")).thenReturn(outlet);

        assertThat(resolver.belongsToInstitution("outlet_user_group", "outlet-1", "inst-1")).isTrue();
    }

    @Test
    @DisplayName("belongsToInstitution returns false when resource missing or tenant mismatch")
    void belongsToInstitutionFalse() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("X").tenantId("other-inst").build();
        when(outletRepository.findById("outlet-1")).thenReturn(outlet);

        assertThat(resolver.belongsToInstitution("outlet_user_group", "outlet-1", "inst-1")).isFalse();
        assertThat(resolver.belongsToInstitution("outlet_user_group", "missing", "inst-1")).isFalse();
    }

    @Test
    @DisplayName("Should resolve every supported user_group type")
    void resolvesAllSupportedTypes() {
        when(courseRepository.findById("c-1")).thenReturn(
                Course.builder().id("c-1").name("Course").tenantId("inst-1").build());
        when(levelRepository.findById("lv-1")).thenReturn(
                Level.builder().id("lv-1").name("Level").tenantId("inst-1").build());
        when(subjectRepository.findById("s-1")).thenReturn(
                Subject.builder().id("s-1").name("Subject").tenantId("inst-1").build());
        when(topicRepository.findById("t-1")).thenReturn(
                Topic.builder().id("t-1").name("Topic").tenantId("inst-1").build());
        when(materialRepository.findById("m-1")).thenReturn(
                Material.builder().id("m-1").name("Material").tenantId("inst-1").build());

        List<GroupResourcePermission> result = resolver.resolveAndFilter(List.of(
                new ResourcePermission("course_user_group", "c-1", List.of("view")),
                new ResourcePermission("level_user_group", "lv-1", List.of("view")),
                new ResourcePermission("subject_user_group", "s-1", List.of("view")),
                new ResourcePermission("topic_user_group", "t-1", List.of("view")),
                new ResourcePermission("material_user_group", "m-1", List.of("view"))
        ), "inst-1");

        assertThat(result).extracting(GroupResourcePermission::displayName)
                .containsExactlyInAnyOrder("Course", "Level", "Subject", "Topic", "Material");
    }
}
