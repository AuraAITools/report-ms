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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserGroupResourceResolver {

    private final InstitutionRepository institutionRepository;
    private final OutletRepository outletRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final AccountRepository accountRepository;
    private final LevelRepository levelRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final MaterialRepository materialRepository;

    public List<GroupResourcePermission> resolveAndFilter(List<ResourcePermission> permissions, String institutionId) {
        List<GroupResourcePermission> resolved = new ArrayList<>();
        for (ResourcePermission rp : permissions) {
            resolveDisplayName(rp.resourceType(), rp.resourceId(), institutionId)
                    .ifPresent(displayName -> resolved.add(new GroupResourcePermission(
                            rp.resourceType(), rp.resourceId(), displayName, rp.permissions())));
        }
        return resolved;
    }

    public boolean belongsToInstitution(String resourceType, String resourceId, String institutionId) {
        return resolveDisplayName(resourceType, resourceId, institutionId).isPresent();
    }

    public List<UserGroupRelation> resolveAndFilterRelations(List<ResourceRelations> relations, String institutionId) {
        List<UserGroupRelation> resolved = new ArrayList<>();
        for (ResourceRelations rr : relations) {
            resolveDisplayName(rr.resourceType(), rr.resourceId(), institutionId)
                    .ifPresent(displayName -> resolved.add(new UserGroupRelation(
                            rr.resourceType(), rr.resourceId(), displayName, rr.relations())));
        }
        return resolved;
    }

    private Optional<String> resolveDisplayName(String resourceType, String resourceId, String institutionId) {
        return switch (resourceType) {
            case "institution_user_group" -> resourceId.equals(institutionId)
                    ? Optional.ofNullable(institutionRepository.findById(resourceId)).map(Institution::getName)
                    : Optional.empty();
            case "outlet_user_group" -> Optional.ofNullable(outletRepository.findById(resourceId))
                    .filter(o -> institutionId.equals(o.getTenantId()))
                    .map(Outlet::getName);
            case "course_user_group" -> Optional.ofNullable(courseRepository.findById(resourceId))
                    .filter(c -> institutionId.equals(c.getTenantId()))
                    .map(Course::getName);
            case "lesson_user_group" -> Optional.ofNullable(lessonRepository.findById(UUID.fromString(resourceId)))
                    .filter(l -> institutionId.equals(l.getTenantId()))
                    .map(Lesson::getName);
            case "account_user_group" -> Optional.ofNullable(accountRepository.findById(resourceId))
                    .filter(a -> institutionId.equals(a.getTenantId()))
                    .map(this::accountDisplayName);
            case "level_user_group" -> Optional.ofNullable(levelRepository.findById(resourceId))
                    .filter(l -> institutionId.equals(l.getTenantId()))
                    .map(Level::getName);
            case "subject_user_group" -> Optional.ofNullable(subjectRepository.findById(resourceId))
                    .filter(s -> institutionId.equals(s.getTenantId()))
                    .map(Subject::getName);
            case "topic_user_group" -> Optional.ofNullable(topicRepository.findById(resourceId))
                    .filter(t -> institutionId.equals(t.getTenantId()))
                    .map(Topic::getName);
            case "material_user_group" -> Optional.ofNullable(materialRepository.findById(resourceId))
                    .filter(m -> institutionId.equals(m.getTenantId()))
                    .map(Material::getName);
            default -> {
                log.warn("Unrecognised user_group resource type, skipping: {}", resourceType);
                yield Optional.empty();
            }
        };
    }

    private String accountDisplayName(Account account) {
        String first = Optional.ofNullable(account.getFirstName()).orElse("");
        String last = Optional.ofNullable(account.getLastName()).orElse("");
        String combined = (first + " " + last).trim();
        return combined.isEmpty() ? account.getId() : combined;
    }
}
