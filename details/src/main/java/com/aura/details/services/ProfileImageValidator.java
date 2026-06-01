package com.aura.details.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates profile image uploads. Pure logic — no Spring, no S3. Throws
 * {@link ProfileImageRejectedException} on any rule violation so the caller can
 * delete the uploaded object and surface a clear error to the client.
 *
 * <p>Note on {@code image/webp}: the JDK's bundled {@link ImageIO} may not include
 * a webp reader, in which case {@link ImageIO#read} returns null and this validator
 * rejects the upload with "could not decode image bytes". That is the correct
 * fail-closed behavior; install a webp ImageReader on the classpath if webp support
 * is required.
 */
@Slf4j
public class ProfileImageValidator {

    public static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg", "image/webp");
    public static final long MAX_BYTES = 5L * 1024 * 1024;
    public static final int MIN_DIMENSION_PX = 200;
    public static final int MAX_DIMENSION_PX = 2048;
    public static final double MAX_ASPECT_RATIO_DEVIATION = 0.1;

    /**
     * Maps an allowed content type to the canonical file extension used in S3 object keys.
     * Throws {@link IllegalArgumentException} for any input not in {@link #ALLOWED_CONTENT_TYPES}
     * — caller should have already validated the content type against the allowlist.
     */
    public static String extensionFor(String contentType) {
        return switch (contentType) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("no extension mapping for " + contentType);
        };
    }

    public ValidationResult validate(byte[] imageBytes, String contentType, long sizeBytes) {
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ProfileImageRejectedException(
                    "content type '" + contentType + "' is not allowed; must be one of " + ALLOWED_CONTENT_TYPES);
        }
        if (sizeBytes > MAX_BYTES) {
            throw new ProfileImageRejectedException(
                    "size " + sizeBytes + " bytes exceeds max " + MAX_BYTES + " bytes");
        }
        BufferedImage img;
        try {
            img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            throw new ProfileImageRejectedException("could not decode image bytes", e);
        }
        if (img == null) {
            throw new ProfileImageRejectedException("could not decode image bytes (unsupported format)");
        }
        int width = img.getWidth();
        int height = img.getHeight();
        if (width < MIN_DIMENSION_PX || height < MIN_DIMENSION_PX) {
            throw new ProfileImageRejectedException(
                    "dimensions " + width + "x" + height + " below min " + MIN_DIMENSION_PX + "px");
        }
        if (width > MAX_DIMENSION_PX || height > MAX_DIMENSION_PX) {
            throw new ProfileImageRejectedException(
                    "dimensions " + width + "x" + height + " above max " + MAX_DIMENSION_PX + "px");
        }
        double ratio = (double) width / (double) height;
        if (ratio < 1.0 - MAX_ASPECT_RATIO_DEVIATION || ratio > 1.0 + MAX_ASPECT_RATIO_DEVIATION) {
            throw new ProfileImageRejectedException(
                    "image aspect ratio " + ratio + " is not squarish (must be within ±"
                            + (MAX_ASPECT_RATIO_DEVIATION * 100) + "% of 1:1)");
        }
        return new ValidationResult(width, height);
    }

    public record ValidationResult(int width, int height) {
    }

    public static class ProfileImageRejectedException extends RuntimeException {
        public ProfileImageRejectedException(String message) {
            super(message);
        }

        public ProfileImageRejectedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
