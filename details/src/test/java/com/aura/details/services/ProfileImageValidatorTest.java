package com.aura.details.services;

import com.aura.details.services.ProfileImageValidator.ProfileImageRejectedException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Profile image validator")
class ProfileImageValidatorTest {

    private final ProfileImageValidator validator = new ProfileImageValidator();

    @DisplayName("Should accept a 400x400 PNG that is exactly square and well under size limit")
    @Test
    void accept_squarePng() throws Exception {
        byte[] png = renderPng(400, 400);
        validator.validate(png, "image/png", png.length);
    }

    @DisplayName("Should accept a 500x550 PNG (10% aspect deviation, within tolerance)")
    @Test
    void accept_squarishPng() throws Exception {
        byte[] png = renderPng(500, 550);
        validator.validate(png, "image/png", png.length);
    }

    @DisplayName("Should reject a 500x650 PNG (30% aspect deviation, outside tolerance)")
    @Test
    void reject_tooTall() throws Exception {
        byte[] png = renderPng(500, 650);
        assertThatThrownBy(() -> validator.validate(png, "image/png", png.length))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("aspect");
    }

    @DisplayName("Should reject a 150x150 PNG (below MIN_DIMENSION_PX)")
    @Test
    void reject_tooSmall() throws Exception {
        byte[] png = renderPng(150, 150);
        assertThatThrownBy(() -> validator.validate(png, "image/png", png.length))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("min");
    }

    @DisplayName("Should reject a 3000x3000 PNG (above MAX_DIMENSION_PX)")
    @Test
    void reject_tooBig() throws Exception {
        byte[] png = renderPng(3000, 3000);
        assertThatThrownBy(() -> validator.validate(png, "image/png", png.length))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("max");
    }

    @DisplayName("Should reject content-type application/pdf even if bytes are otherwise valid")
    @Test
    void reject_disallowedContentType() throws Exception {
        byte[] png = renderPng(400, 400);
        assertThatThrownBy(() -> validator.validate(png, "application/pdf", png.length))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("content type");
    }

    @DisplayName("Should reject content-type image/svg+xml (security exclusion)")
    @Test
    void reject_svg() throws Exception {
        byte[] png = renderPng(400, 400);
        assertThatThrownBy(() -> validator.validate(png, "image/svg+xml", png.length))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("content type");
    }

    @DisplayName("Should reject when sizeBytes exceeds 5 MB even if image dimensions are fine")
    @Test
    void reject_oversize() throws Exception {
        byte[] png = renderPng(400, 400);
        long fakeSize = ProfileImageValidator.MAX_BYTES + 1;
        assertThatThrownBy(() -> validator.validate(png, "image/png", fakeSize))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("size");
    }

    @DisplayName("Should reject bytes that are not a decodable image even if content-type says image/png")
    @Test
    void reject_notAnImage() {
        byte[] garbage = "hello world".getBytes();
        assertThatThrownBy(() -> validator.validate(garbage, "image/png", garbage.length))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("decode");
    }

    @DisplayName("Should return the dimensions used during validation")
    @Test
    void returnsDimensions_onAccept() throws Exception {
        byte[] png = renderPng(400, 400);
        ProfileImageValidator.ValidationResult result = validator.validate(png, "image/png", png.length);
        assertThat(result.width()).isEqualTo(400);
        assertThat(result.height()).isEqualTo(400);
    }

    @DisplayName("Should map allowed content types to canonical extensions")
    @Test
    void extensionFor_returnsCanonicalExtensions() {
        assertThat(ProfileImageValidator.extensionFor("image/png")).isEqualTo("png");
        assertThat(ProfileImageValidator.extensionFor("image/jpeg")).isEqualTo("jpg");
        assertThat(ProfileImageValidator.extensionFor("image/webp")).isEqualTo("webp");
    }

    @DisplayName("Should throw IllegalArgumentException for unmapped content types")
    @Test
    void extensionFor_throwsForUnmapped() {
        assertThatThrownBy(() -> ProfileImageValidator.extensionFor("application/pdf"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("application/pdf");
    }

    private static byte[] renderPng(int w, int h) throws IOException {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, w, h);
        g.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }
}
