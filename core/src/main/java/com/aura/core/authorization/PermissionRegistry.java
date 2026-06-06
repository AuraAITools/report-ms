package com.aura.core.authorization;

import java.util.List;
import java.util.Map;

public final class PermissionRegistry {

    private PermissionRegistry() {
    }

    public static final Map<String, List<String>> RESOURCE_PERMISSIONS = Map.ofEntries(
            Map.entry("system", List.of("view", "update", "delete", "create_institution")),
            Map.entry("institution", List.of("view", "update", "delete", "create_account", "create_outlet", "create_level", "create_subject", "create_topic", "create_material", "create_school", "delete_school")),
            Map.entry("outlet", List.of("view", "update", "delete", "create_course")),
            Map.entry("course", List.of("view", "update", "delete", "create_lesson")),
            Map.entry("lesson", List.of("view", "update", "delete", "create_lesson_plan")),
            Map.entry("account", List.of("view", "update", "delete")),
            Map.entry("level", List.of("view", "update", "delete")),
            Map.entry("subject", List.of("view", "update", "delete")),
            Map.entry("topic", List.of("view", "update", "delete")),
            Map.entry("material", List.of("view", "update", "delete"))
    );

    private static final List<String> USER_GROUP_PERMISSION_SET = List.of(
            "group_view",
            "group_add_viewer", "group_add_admin", "group_add_modifier",
            "group_remove_viewer", "group_remove_admin", "group_remove_modifier",
            "group_add_group_admin", "group_remove_group_admin",
            "group_add_group_viewer", "group_remove_group_viewer",
            "view", "update", "delete", "is_admin_or_modifier"
    );

    public static final Map<String, List<String>> USER_GROUP_PERMISSIONS = Map.ofEntries(
            Map.entry("institution_user_group", USER_GROUP_PERMISSION_SET),
            Map.entry("outlet_user_group", USER_GROUP_PERMISSION_SET),
            Map.entry("course_user_group", USER_GROUP_PERMISSION_SET),
            Map.entry("lesson_user_group", USER_GROUP_PERMISSION_SET),
            Map.entry("material_user_group", USER_GROUP_PERMISSION_SET),
            Map.entry("account_user_group", USER_GROUP_PERMISSION_SET)
    );

    public static final String SYSTEM_RESOURCE_TYPE = "system";
    public static final String SYSTEM_RESOURCE_ID = "aura";
}
