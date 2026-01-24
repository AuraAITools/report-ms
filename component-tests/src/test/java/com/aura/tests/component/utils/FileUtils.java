package com.aura.tests.component.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class FileUtils {
    private FileUtils() {
    }

    public static String findGraphqlQueryFromFiles(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("files/" + filename);
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GraphQL file: " + filename, e);
        }
    }
}
