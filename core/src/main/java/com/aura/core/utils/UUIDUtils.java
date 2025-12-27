package com.aura.core.utils;

import java.util.regex.Pattern;

public class UUIDUtils {
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    private UUIDUtils() {
    }

    public static boolean isUUid(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }
}
